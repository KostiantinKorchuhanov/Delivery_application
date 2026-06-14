package com.example.util;

import com.example.config.HibernateConfig;
import com.example.entity.order.OrderInfo;
import com.example.entity.order.Orders;
import com.example.entity.restaurant.Dish;
import com.example.entity.restaurant.Restaurant;
import com.example.entity.user.*;
import com.example.validation.subvalidation.PasswordValidation;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataInitializer {

    public static void main(String[] args) {
        EntityManager em = HibernateConfig.getEntityManager();
        String passHash = PasswordValidation.hashPassword("1111");

        try {
            em.getTransaction().begin();

            Admin admin = new Admin();
            setupUser(admin, "admin", "Big", "Boss", "admin@email.com", passHash);
            em.persist(admin);

            List<Customer> customers = new ArrayList<>();
            String[] names = {"John", "Emma", "Michael", "Sophia", "William"};
            String[] surnames = {"Doe", "Smith", "Johnson", "Brown", "Wilson"};

            for (int i = 0; i < 5; i++) {
                Customer c = new Customer();
                setupUser(c, "customer" + (i + 1), names[i], surnames[i], names[i].toLowerCase() + "@email.com", passHash);
                c.setBonusPoints(100);
                em.persist(c);
                customers.add(c);
            }

            List<Driver> drivers = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                Driver d = new Driver();
                setupUser(d, "driver" + (i + 1), "DriverName" + i, "Fast", "driver" + i + "@email.com", passHash);
                d.setAverageRating(4.8);
                em.persist(d);
                drivers.add(d);
            }

            List<Restaurant> restaurants = new ArrayList<>();
            String[] resNames = {"Burger King", "Pizza Hut", "Sushi Master", "Pasta Place", "Taco Bell"};

            for (int i = 0; i < 5; i++) {
                RestaurantOwner owner = new RestaurantOwner();
                setupUser(owner, "owner" + (i + 1), "Owner" + i, "Res", "owner" + i + "@email.com", passHash);
                em.persist(owner);

                Restaurant r = new Restaurant();
                r.setRestaurantName(resNames[i]);
                r.setAddress("Street " + (i + 10));
                r.setDescription("Best " + resNames[i] + " in town!");
                r.setOwner(owner);
                em.persist(r);
                restaurants.add(r);

                for (int j = 1; j <= 3; j++) {
                    Dish dish = new Dish();
                    dish.setDishName(resNames[i] + " Special " + j);
                    dish.setPrice(10.0 + j * 5);
                    dish.setRestaurant(r);
                    dish.setAvailable(true);
                    em.persist(dish);
                    if (r.getMenu() == null) r.setMenu(new ArrayList<>());
                    r.getMenu().add(dish);
                }
            }

            String[] statuses = {"COMPLETED", "PENDING", "DELIVERED", "COMPLETED", "PENDING"};
            Random rand = new Random();

            for (int i = 0; i < 5; i++) {
                Orders order = new Orders();
                order.setCustomer(customers.get(i));
                order.setRestaurant(restaurants.get(i));
                order.setDriver(drivers.get(i));
                order.setStatus(statuses[i]);
                order.setCreatedAt(LocalDateTime.now().minusDays(rand.nextInt(7)).minusHours(rand.nextInt(24)));
                OrderInfo info = new OrderInfo();
                Dish selectedDish = restaurants.get(i).getMenu().get(0);
                info.setDish(selectedDish);
                info.setQuantity(2);
                info.setPrice(selectedDish.getPrice());

                order.addOrderItem(info);
                em.persist(order);
            }

            em.getTransaction().commit();
            System.out.println("Success! Fake data generated.");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private static void setupUser(User user, String username, String name, String surname, String email, String pass) {
        user.setUsername(username);
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setPasswordHash(pass);
        user.setPhoneNumber("123456789");
    }
}