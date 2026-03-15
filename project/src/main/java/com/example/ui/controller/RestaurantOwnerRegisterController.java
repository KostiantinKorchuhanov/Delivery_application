package com.example.ui.controller;

import com.example.app.NavigationManager;
import com.example.entity.restaurant.Restaurant;
import com.example.entity.user.RestaurantOwner;
import com.example.service.RestaurantOwnerService;
import com.example.session.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class RestaurantOwnerRegisterController {
    @FXML TextField restaurantNameField;
    @FXML TextField restaurantAddressField;
    @FXML TextArea restaurantDescriptionField;
    @FXML Button addButton;

    @FXML private void addNewRestaurant(){
        RestaurantOwner  restaurantOwner = (RestaurantOwner) UserSession.getCurrentUser();
        String name = restaurantNameField.getText();
        String address = restaurantAddressField.getText();
        String description = restaurantDescriptionField.getText();
        RestaurantOwnerService restaurantOwnerService = new RestaurantOwnerService();
        restaurantOwnerService.saveNewRestaurant(name, description, address, restaurantOwner.getUserId());
        NavigationManager.closeCurrentStage(addButton);
    }
}
