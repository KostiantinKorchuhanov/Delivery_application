package com.example.ui.controller;

import com.example.app.NavigationManager;
import com.example.entity.restaurant.Restaurant;
import com.example.entity.user.RestaurantOwner;
import com.example.service.RestaurantOwnerService;
import com.example.session.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;

public class RestaurantOwnerController {
    @FXML private FlowPane restaurantFlowPane;
    @FXML private Button myRestaurantsButton;


    @FXML private void openRestaurants(){
        RestaurantOwner restaurantOwner = (RestaurantOwner) UserSession.getCurrentUser();
        RestaurantOwnerService restaurantOwnerService = new RestaurantOwnerService();
        List<Restaurant> restaurantList = restaurantOwnerService.findAllRestaurants(restaurantOwner.getUserId());
        restaurantFlowPane.getChildren().clear();
        restaurantFlowPane.getChildren().clear();
        for (Restaurant res : restaurantList) {
            VBox card = new VBox(10);
            card.getStyleClass().add("restaurant-card");
            Text name = new Text(res.getRestaurantName());
            name.getStyleClass().add("restaurant-name");
            Text description = new Text(res.getDescription());
            description.getStyleClass().add("restaurant-description");
            description.setWrappingWidth(200);
            Text address = new Text(res.getAddress());
            address.getStyleClass().add("restaurant-address");
            card.getChildren().addAll(name, description, address);
            restaurantFlowPane.getChildren().add(card);
        }
        myRestaurantsButton.setDisable(true);
    }

    @FXML private void openStatistics(){
    }

    @FXML private void addRestaurant() throws IOException {
        NavigationManager.navigateToNewRestaurant();
        openRestaurants();
    }

    @FXML private void deleteRestaurant(){
    }
}
