package com.example.app;

import com.example.config.HibernateConfig;
import com.example.entity.order.OrderInfo;
import com.example.entity.order.Orders;
import com.example.entity.restaurant.Dish;
import com.example.entity.restaurant.Restaurant;
import com.example.entity.user.Customer;
import com.example.entity.user.RestaurantOwner;
import com.example.service.UserService;
import com.example.ui.controller.ChooseUserController;
import jakarta.persistence.EntityManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage enterStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/example/app/ui/choose-user.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 650);
        scene.getStylesheets().add(getClass().getResource("/com/example/app/styles/login-style.css").toExternalForm());
        enterStage.setTitle("hehehe");
        enterStage.setScene(scene);
        enterStage.setMinWidth(800);
        enterStage.setMinHeight(650);
        enterStage.show();

        // This is soooo stupid DO NOT FORGET TO DELETE THIS DISGUSTING THING
        ChooseUserController controller = fxmlLoader.<ChooseUserController>getController();
        controller.dirtyOperation();

//        UserService userService = new UserService();
//
//        for (int i = 1; i <= 50; i++) {
//            userService.registerUser(
//                    "NameRestaurant" + i,
//                    "SurnameRestaurant" + i,
//                    "usernameRestaurant" + i,
//                    "userRestaurant" + i + "@example.com",
//                    "Password" + i,
//                    "Password" + i,
//                    "123456789",
//                    "Restaurant"
//            );
//        }
    }

    public static void main(String[] args) {
//        EntityManager em = HibernateConfig.getEntityManager();
//        try {
//            em.getTransaction().begin();
//
//            long timestamp = System.currentTimeMillis();
//
//            // 1. Создаем владельца (RestaurantOwner)
//            RestaurantOwner owner = new RestaurantOwner();
//            owner.setName("Admin");
//            owner.setSurname("Owner");
//            owner.setUsername("owner"); // ОБЯЗАТЕЛЬНО
//            owner.setEmail("owner"  + "@test.com"); // ОБЯЗАТЕЛЬНО
//            owner.setPhoneNumber("123456789"); // Ровно 9 символов, как в Entity
//            owner.setPasswordHash("1111");
//            em.persist(owner);
//
//            // 2. Создаем клиента (Customer)
//            Customer customer = new Customer();
//            customer.setName("Guest");
//            customer.setSurname("User");
//            customer.setUsername("customer"); // ОБЯЗАТЕЛЬНО
//            customer.setEmail("customer" + "@test.com"); // ОБЯЗАТЕЛЬНО
//            customer.setPhoneNumber("987654321");
//            customer.setPasswordHash("1111");
//            em.persist(customer);
//
//            // 3. Создаем ресторан
//            Restaurant res = new Restaurant();
//            res.setRestaurantName("Best Pizza " + timestamp);
//            res.setAddress("Main St 10");
//            res.setOwner(owner);
//            em.persist(res);
//
//            // 4. Создаем блюдо
//            Dish pizza = new Dish();
//            pizza.setDishName("Margherita");
//            pizza.setPrice(12.5);
//            pizza.setRestaurant(res);
//            pizza.setAvailable(true);
//            em.persist(pizza);
//
//            // 5. Генерируем 10 заказов
//            for (int i = 1; i <= 10; i++) {
//                Orders order = new Orders();
//                order.setCustomer(customer);
//                order.setRestaurant(res);
//                order.setStatus("PENDING");
//
//                // СНАЧАЛА сохраняем пустой заказ, чтобы он получил ID от БД
//                em.persist(order);
//                // ПРИНУДИТЕЛЬНО отправляем в БД, чтобы ID зафиксировался
//                em.flush();
//
//                OrderInfo info = new OrderInfo();
//                info.setDish(pizza);
//                info.setQuantity(i);
//                info.setPrice(pizza.getPrice());
//
//                // Теперь добавляем позицию — теперь у 'order' есть ID, и OrderInfo не упадет
//                order.addOrderItem(info);
//            }
//
//            em.getTransaction().commit();
//            System.out.println("--- УСПЕХ: 10 заказов создано! ---");
//        } catch (Exception e) {
//            if (em.getTransaction().isActive()) em.getTransaction().rollback();
//            System.err.println("--- ОШИБКА ГЕНЕРАЦИИ ---");
//            e.printStackTrace();
//        } finally {
//            em.close();
//        }
        launch();
    }
}
