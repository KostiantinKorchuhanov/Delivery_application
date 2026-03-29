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
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/ui/choose-user.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 650);
        scene.getStylesheets().add(getClass().getResource("/styles/login-style.css").toExternalForm());
        enterStage.setTitle("hehehe");
        enterStage.setScene(scene);
        enterStage.setMinWidth(800);
        enterStage.setMinHeight(650);
        enterStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
