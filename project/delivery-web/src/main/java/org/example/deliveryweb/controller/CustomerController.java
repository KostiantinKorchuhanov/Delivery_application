package org.example.deliveryweb.controller;

import com.example.entity.order.OrderInfo;
import com.example.entity.order.Orders;
import com.example.entity.restaurant.Dish;
import com.example.entity.restaurant.Restaurant;
import com.example.entity.restaurant.RestaurantReview;
import com.example.entity.user.Customer;
import com.example.entity.user.Driver;
import com.example.entity.user.User;
import jakarta.servlet.http.HttpSession;
import org.example.deliveryweb.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("cart")
public class CustomerController {

    private final DishRepository dishRepository;
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final CustomerRepository customerRepository;
    private final DriverRepository driverRepository;
    private final RestaurantReviewRepository reviewRepository;


    public CustomerController(DishRepository dishRepository, RestaurantReviewRepository restaurantReviewRepository, DriverRepository driverRepository, OrderRepository orderRepository, RestaurantRepository restaurantRepository, CustomerRepository customerRepository) {
        this.dishRepository = dishRepository;
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
        this.customerRepository = customerRepository;
        this.driverRepository = driverRepository;
        this.reviewRepository = restaurantReviewRepository;
    }

    @ModelAttribute("cart")
    public List<Dish> cart() {
        return new ArrayList<>();
    }

    @GetMapping("/customer/restaurants")
    public String restaurantsPage(Model model, @ModelAttribute("cart") List<Dish> cart) {
        model.addAttribute("restaurants", restaurantRepository.findAll());
        model.addAttribute("cartSize", cart.size());
        return "restaurant_page";
    }

    @GetMapping("/customer/restaurant/{id}")
    public String dishesByRestaurant(@PathVariable("id") int id, Model model,
                                     @ModelAttribute("cart") List<Dish> cart, HttpSession session) {
        Restaurant restaurant = restaurantRepository.findById(id).orElse(null);
        if (restaurant == null) {
            return "redirect:/customer/restaurants";
        }

        User user = (User) session.getAttribute("user");
        boolean hasReviewed = reviewRepository.existsByUserAndRestaurant(user, restaurant);
        List<Dish> dishes = dishRepository.findDishByRestaurant(restaurant);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("dishes", dishes);
        model.addAttribute("cartSize", cart.size());
        model.addAttribute("hasReviewed", hasReviewed);
        return "restaurant_dishes";
    }

    @PostMapping("/customer/cart/add")
    public String addToCart(@RequestParam("dishId") int dishId, @RequestParam("quantity") int quantity, @ModelAttribute("cart") List<Dish> cart) {
        if (quantity < 1) quantity = 1;
        if (quantity > 10) quantity = 10;

        Dish dish = dishRepository.findById(dishId).orElse(null);
        if (dish != null) {
            for (int i = 0; i < quantity; i++) {
                cart.add(dish);
            }
            return "redirect:/customer/restaurant/" + dish.getRestaurant().getRestaurantId();
        }
        return "redirect:/customer/restaurants";
    }

    @GetMapping("/customer/cart")
    public String viewCart(Model model, @ModelAttribute("cart") List<Dish> cart) {
        Map<Dish, Long> groupedCart = cart.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        model.addAttribute("groupedCart", groupedCart);
        model.addAttribute("cartSize", cart.size());
        return "cart_page";
    }

    @PostMapping("/customer/cart/remove")
    public String removeFromCart(@RequestParam("dishId") int dishId, @ModelAttribute("cart") List<Dish> cart) {
        cart.stream()
                .filter(d -> d.getDishId() == dishId)
                .findFirst()
                .ifPresent(cart::remove);
        return "redirect:/customer/cart";
    }

    @PostMapping("/customer/order/checkout")
    public String checkout(@ModelAttribute("cart") List<Dish> cart, @RequestParam("useBonuses") int useBonuses, HttpSession session) {
        if (cart.isEmpty()) {
            return "redirect:/customer/cart?error=empty";
        }
        Customer customer = (Customer) session.getAttribute("user");
        Orders order = new Orders();
        order.setCustomer(customer);
        order.setBonusesUsed(useBonuses);
        order.setStatus("PENDING");
        order.setRestaurant(cart.get(0).getRestaurant());
        Map<Dish, Long> groupedCart = cart.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        for (Map.Entry<Dish, Long> entry : groupedCart.entrySet()) {
            OrderInfo item = new OrderInfo();
            item.setDish(entry.getKey());
            item.setPrice(entry.getKey().getActivePrice());
            item.setQuantity(entry.getValue().intValue());
            order.addOrderItem(item);
        }
        orderRepository.save(order);
        customer.setBonusPoints(customer.getBonusPoints() - useBonuses);
        customerRepository.save(customer);

        cart.clear();
        return "redirect:/customer/restaurants?ordered";
    }

    @PostMapping("/customer/order/complete")
    public String completeOrder(@RequestParam("orderId") int orderId,
                                @RequestParam("rating") int rating,
                                @RequestParam("review") String review) {
        Orders order = orderRepository.findById(orderId).orElseThrow();

        if ("DELIVERED".equals(order.getStatus())) {
            order.setStatus("COMPLETED");
            order.setDriverRating(rating);
            order.setReview(review);
            Driver driver = (Driver) order.getDriver();
            if (driver != null) {
                driver.addRating(rating);
                driver.setCurrentOrder(null);
                driverRepository.save(driver);
            }

            Customer customer = (Customer) order.getCustomer();
            int earnedBonuses = (int) (order.getOrderTotalPrice() * 0.1);
            customer.setBonusPoints(customer.getBonusPoints() + earnedBonuses);
            customerRepository.save(customer);

            orderRepository.save(order);
        }
        return "redirect:/customer/restaurants?success";
    }

    @GetMapping("/customer/orders/my")
    public String viewMyOrders(@RequestParam(value = "sort", required = false, defaultValue = "newest") String sort,
                               Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<Orders> orders = orderRepository.findByCustomer(user);

        if ("oldest".equals(sort)) {
            orders.sort((o1, o2) -> Integer.compare(o1.getOrderId(), o2.getOrderId()));
        } else {
            orders.sort((o1, o2) -> Integer.compare(o2.getOrderId(), o1.getOrderId()));
        }

        model.addAttribute("myOrders", orders);
        model.addAttribute("currentSort", sort);
        return "my_orders_page";
    }

    @GetMapping("/customer/restaurants/search")
    public String searchRestaurants(@RequestParam("query") String query, Model model, @ModelAttribute("cart") List<Dish> cart) {
        List<Restaurant> found = restaurantRepository.findByRestaurantNameContainingIgnoreCase(query);
        model.addAttribute("restaurants", found);
        model.addAttribute("cartSize", cart.size());
        model.addAttribute("searchQuery", query);
        return "restaurant_page";
    }

    @GetMapping("/customer/restaurant/{id}/search")
    public String searchDishes(@PathVariable("id") int id, @RequestParam("query") String query, Model model, @ModelAttribute("cart") List<Dish> cart) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow();
        List<Dish> foundDishes = dishRepository.findByRestaurantAndDishNameContainingIgnoreCase(restaurant, query);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("dishes", foundDishes);
        model.addAttribute("cartSize", cart.size());
        model.addAttribute("dishQuery", query);
        return "restaurant_dishes";
    }

    @PostMapping("/customer/restaurant/add-review")
    public String addRestaurantReview(@RequestParam("restaurantId") int restaurantId,
                                      @RequestParam("rating") int rating,
                                      @RequestParam("comment") String comment,
                                      HttpSession session) {

        User user = (User) session.getAttribute("user");
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid restaurant Id"));

        if (reviewRepository.existsByUserAndRestaurant(user, restaurant)) {
            return "redirect:/customer/restaurant/" + restaurantId + "?alreadyReviewed";
        }

        RestaurantReview review = new RestaurantReview();
        review.setUser(user);
        review.setRestaurant(restaurant);
        review.setRating(rating);
        review.setComment(comment);

        reviewRepository.save(review);

        return "redirect:/customer/restaurant/" + restaurantId;
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "about_page";
    }
}