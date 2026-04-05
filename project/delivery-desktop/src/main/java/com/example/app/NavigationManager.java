package com.example.app;

import com.example.entity.restaurant.Dish;
import com.example.entity.restaurant.Restaurant;
import com.example.entity.user.Admin;
import com.example.entity.user.RestaurantOwner;
import com.example.entity.user.User;
import com.example.session.UserSession;
import com.example.ui.controller.RestaurantDishController;
import com.example.ui.controller.RestaurantOwnerRegisterController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class NavigationManager {
    public static void navigateToUserList(User user) throws IOException {
        UserSession.login(user);
        String fxmlPath;
        String cssPath;
        String userType;

        if (user instanceof Admin) {
            fxmlPath = "/ui/admin-main.fxml";
            cssPath = "/styles/admin-style.css";
            userType = "Admin";
        } else if (user instanceof RestaurantOwner) {
            fxmlPath = "/ui/restaurant-owner-main.fxml";
            cssPath = "/styles/restaurant-owner-style.css";
            userType = "Restaurant Owner";
        } else {
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

    public static void navigateToLogout() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/ui/choose-user.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 650);
        scene.getStylesheets().add(Main.class.getResource("/styles/login-style.css").toExternalForm());
        stage.setTitle("hehehe");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(650);
        stage.show();
    }

    public static void navigateToRegistration() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/ui/admin-register.fxml"));
        Scene scene = new Scene(loader.load(), 300, 450);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setTitle("Add a new user");
        stage.showAndWait();
    }

    public static void navigateToDelete() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/ui/admin-delete.fxml"));
        Scene scene = new Scene(loader.load(), 300, 150);
        scene.getStylesheets().add(NavigationManager.class.getResource("/styles/delete-user-style.css").toExternalForm());
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setTitle("Add a new user");
        stage.showAndWait();
    }

    public static void navigateToNewRestaurant() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/ui/restaurant-register.fxml"));
        Scene scene = new Scene(loader.load(), 400, 450);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setTitle("Add a new restaurant");
        stage.showAndWait();
    }

    public static void navigateToEditRestaurant(Restaurant restaurant, Runnable runnable) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/ui/restaurant-register.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        RestaurantOwnerRegisterController controller = loader.getController();
        controller.setExistingRestaurant(restaurant);
        controller.setRefreshCallback(runnable);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setMaximized(true);
        stage.showAndWait();
    }

    public static void navigateToDish(Restaurant restaurant) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/ui/restaurant-add-dish.fxml"));
        Scene scene = new Scene(loader.load(), 400, 400);
        stage.setScene(scene);

        RestaurantDishController controller = loader.getController();
        controller.setRestaurant(restaurant);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.showAndWait();
    }

    public static void navigateToEditDish(Dish dish) throws IOException {
        FXMLLoader loader = new FXMLLoader(NavigationManager.class.getResource("/ui/restaurant-add-dish.fxml"));
        Parent root = loader.load();
        RestaurantDishController controller = loader.getController();
        controller.setDishData(dish);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.showAndWait();
    }

    public static void closeCurrentStage(Node node) {
        Stage currentStage = (Stage) node.getScene().getWindow();
        currentStage.close();
    }

    public static void navigateToOrderCreation() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/ui/admin-order.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(NavigationManager.class.getResource("/styles/add-order.css").toExternalForm());
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setMaximized(true);
        stage.setTitle("Add a new order");
        stage.showAndWait();
    }
}

