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
    @FXML Button deleteButton;

    private Restaurant editRestaurant;
    private Runnable runnable;

    @FXML public void initialize(){
        deleteButton.setDisable(true);
    }

    public void setRefreshCallback(Runnable runnable) {
        this.runnable = runnable;
    }

    public void setExistingRestaurant(Restaurant restaurant) {
        this.editRestaurant = restaurant;
        restaurantNameField.setText(restaurant.getRestaurantName());
        restaurantDescriptionField.setText(restaurant.getDescription());
        restaurantAddressField.setText(restaurant.getAddress());
        deleteButton.setDisable(false);
    }

    @FXML private void addNewRestaurant(){
        if (editRestaurant == null){
            deleteButton.setDisable(true);
            RestaurantOwner  restaurantOwner = (RestaurantOwner) UserSession.getCurrentUser();
            String name = restaurantNameField.getText();
            String address = restaurantAddressField.getText();
            String description = restaurantDescriptionField.getText();
            RestaurantOwnerService restaurantOwnerService = new RestaurantOwnerService();
            restaurantOwnerService.saveNewRestaurant(name, description, address, restaurantOwner.getUserId());
        }
        else {
            deleteButton.setDisable(false);
            deleteButton.setOnAction(event -> {
                deleteRestaurant();
            });
            editRestaurant.setRestaurantName(restaurantNameField.getText());
            editRestaurant.setDescription(restaurantDescriptionField.getText());
            editRestaurant.setAddress(restaurantAddressField.getText());
            new RestaurantOwnerService().updateRestaurant(editRestaurant);
        }
        if (runnable != null){
            runnable.run();
        }
        finishWork();
        NavigationManager.closeCurrentStage(addButton);
    }

    @FXML private void deleteRestaurant(){
        if (editRestaurant != null) {
            RestaurantOwnerService restaurantOwnerService = new RestaurantOwnerService();
            restaurantOwnerService.deleteRestaurant(editRestaurant.getRestaurantId());
            finishWork();
            NavigationManager.closeCurrentStage(deleteButton);
        }
    }

    private void finishWork(){
        if (runnable != null){
            runnable.run();
        }
    }
}
