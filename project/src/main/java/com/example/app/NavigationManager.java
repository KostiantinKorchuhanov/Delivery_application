package com.example.app;

import com.example.entity.user.Admin;
import com.example.entity.user.RestaurantOwner;
import com.example.entity.user.User;
import com.example.service.UserService;
import jakarta.persistence.EntityManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NavigationManager {
    public static void navigateToUserList(User user) throws IOException {
        String fxmlPath;
        String cssPath;
        String userType;

        if (user instanceof Admin){
            fxmlPath = "/com/example/app/ui/admin-main.fxml";
            cssPath = "/com/example/app/styles/admin-style.css";
            userType = "Admin";
        } else if (user instanceof RestaurantOwner) {
            fxmlPath = "/com/example/app/ui/restaurant-owner-main.fxml";
            cssPath = "/com/example/app/styles/restaurant-owner-style.css";
            userType = "Restaurant Owner";
        }
        else {
            return;
        }
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlPath));
        Scene scene = new Scene(loader.load(), 800, 650);
        scene.getStylesheets().add(NavigationManager.class.getResource(cssPath).toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Welcome, my cute " + userType);
        stage.setMinWidth(800);
        stage.setMinHeight(650);
        stage.show();
    }
}
