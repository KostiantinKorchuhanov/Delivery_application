package com.example.ui.controller;

import com.example.app.NavigationManager;
import com.example.entity.restaurant.Dish;
import com.example.entity.restaurant.Restaurant;
import com.example.service.RestaurantService;
import com.example.ui.helper.AlertWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class RestaurantDishController {
    @FXML
    private TextField dishNameField;
    @FXML
    private TextField dishPriceField;
    @FXML
    private TextArea dishDescriptionField;
    @FXML
    private CheckBox dishAvialable;
    @FXML
    private Button addDishButton;
    @FXML
    private Button deleteDishButton;


    private Dish editingDish;
    private Restaurant currentRestaurant;
    private final RestaurantService restaurantService = new RestaurantService();

    @FXML
    private void initialize() {
        deleteDishButton.setVisible(false);
    }

    public void setRestaurant(Restaurant restaurant) {
        this.currentRestaurant = restaurant;
    }

    public void setDishData(Dish dish) {
        this.editingDish = dish;
        this.currentRestaurant = dish.getRestaurant();

        dishNameField.setText(dish.getDishName());
        dishPriceField.setText(String.valueOf(dish.getPrice()));
        dishDescriptionField.setText(dish.getDescription());
        dishAvialable.setSelected(dish.isAvailable());
        addDishButton.setText("Update Dish");
        deleteDishButton.setVisible(true);
    }

    @FXML
    private void addNewDish(ActionEvent event) {
        if (dishNameField.getText().isEmpty() || dishPriceField.getText().isEmpty()) {
            AlertWindow.showError("Validation Error", "Name and Price are required!");
            return;
        }

        try {
            if (editingDish == null) {
                Dish dish = new Dish();
                fillDishData(dish);
                restaurantService.addDish(currentRestaurant.getRestaurantId(), dish);
                AlertWindow.showInformation("Success", "Dish added successfully!");
            } else {
                fillDishData(editingDish);
                restaurantService.updateDish(editingDish);
                AlertWindow.showInformation("Success", "Dish updated successfully!");
            }
            NavigationManager.closeCurrentStage(addDishButton);
        } catch (NumberFormatException e) {
            AlertWindow.showError("Format Error", "Price must be a valid number (e.g., 12.50)");
        } catch (Exception e) {
            AlertWindow.showError("Database Error", "Could not save dish details.");
            e.printStackTrace();
        }
    }

    private void fillDishData(Dish dish) {
        dish.setDishName(dishNameField.getText());
        dish.setPrice(Double.parseDouble(dishPriceField.getText()));
        dish.setDescription(dishDescriptionField.getText());
        dish.setAvailable(dishAvialable.isSelected());
        dish.setRestaurant(currentRestaurant);
    }

    public void deleteDish(ActionEvent actionEvent) {
        if (editingDish != null) {
            boolean confirmed = AlertWindow.showConfirmation(
                    "Delete Dish",
                    "Are you sure you want to delete " + editingDish.getDishName() + "?"
            );
            if (confirmed) {
                try {
                    restaurantService.deleteDish(editingDish);
                    AlertWindow.showInformation("Deleted", "Dish has been removed.");
                    NavigationManager.closeCurrentStage(deleteDishButton);
                } catch (Exception e) {
                    AlertWindow.showError("Error", "Could not delete dish.");
                }
            }
        }
    }
}
