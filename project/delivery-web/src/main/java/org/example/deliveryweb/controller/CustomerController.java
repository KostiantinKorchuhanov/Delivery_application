package org.example.deliveryweb.controller;

import com.example.entity.order.OrderInfo;
import com.example.entity.order.Orders;
import com.example.entity.restaurant.Dish;
import org.example.deliveryweb.repository.DishRepository;
import org.example.deliveryweb.repository.OrderRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes("cart")
public class CustomerController {

    private final DishRepository dishRepository;
    private final OrderRepository orderRepository;

    public CustomerController(DishRepository dishRepository, OrderRepository orderRepository) {
        this.dishRepository = dishRepository;
        this.orderRepository = orderRepository;
    }

    @ModelAttribute("cart")
    public List<Dish> cart() {
        return new ArrayList<>();
    }

    @GetMapping("/customer/home")
    public String customerPage(Model model, @ModelAttribute("cart") List<Dish> cart) {
        model.addAttribute("dishes", dishRepository.findAll());
        model.addAttribute("cartSize", cart.size());
        return "customer_page";
    }

    @PostMapping("/customer/cart/add")
    public String addToCart(@RequestParam("dishId") int dishId, @ModelAttribute("cart") List<Dish> cart) {
        dishRepository.findById(dishId).ifPresent(cart::add);
        return "redirect:/customer/home";
    }

    @PostMapping("/customer/cart/remove")
    public String removeFromCart(@RequestParam("dishId") int dishId, @ModelAttribute("cart") List<Dish> cart) {
        cart.stream()
                .filter(d -> d.getDishId() == dishId)
                .findFirst()
                .ifPresent(cart::remove);
        return "redirect:/customer/home";
    }

    @PostMapping("/customer/order/checkout")
    public String checkout(@ModelAttribute("cart") List<Dish> cart) {
        if (cart.isEmpty()) return "redirect:/customer/home";

        Orders order = new Orders();
        order.setStatus("PENDING");

        for (Dish dish : cart) {
            OrderInfo item = new OrderInfo();
            item.setDish(dish);
            item.setPrice(dish.getPrice());
            item.setQuantity(1);
            order.addOrderItem(item);
        }

        orderRepository.save(order);
        cart.clear();
        return "redirect:/customer/home?ordered";
    }
}