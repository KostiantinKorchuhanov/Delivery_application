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

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class RestaurantDishController {
    private final RestaurantService restaurantService = new RestaurantService();
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
    @FXML
    private TextField specialPriceField;
    @FXML
    private TextField specialStartField;
    @FXML
    private TextField specialEndField;

    private Dish editingDish;
    private Restaurant currentRestaurant;

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

        if (dish.getSpecialPrice() != null) {
            specialPriceField.setText(String.valueOf(dish.getSpecialPrice()));
        }
        if (dish.getSpecialPriceStart() != null) {
            specialStartField.setText(dish.getSpecialPriceStart().toString());
        }
        if (dish.getSpecialPriceEnd() != null) {
            specialEndField.setText(dish.getSpecialPriceEnd().toString());
        }
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

    private void fillDishData(Dish dish) throws DateTimeParseException, IllegalArgumentException {
        dish.setDishName(dishNameField.getText());
        dish.setPrice(Double.parseDouble(dishPriceField.getText()));
        dish.setDescription(dishDescriptionField.getText());
        dish.setAvailable(dishAvialable.isSelected());
        dish.setRestaurant(currentRestaurant);

        String sPrice = specialPriceField.getText();
        if (sPrice != null && !sPrice.isEmpty()) {
            dish.setSpecialPrice(Double.parseDouble(sPrice));

            LocalTime start = LocalTime.parse(specialStartField.getText());
            LocalTime end = LocalTime.parse(specialEndField.getText());

            if (!end.isAfter(start)) {
                AlertWindow.showError("Validation Error", "End time must be after start time");
                throw new IllegalArgumentException("End time must be after start time.");
            }

            dish.setSpecialPriceStart(start);
            dish.setSpecialPriceEnd(end);
        } else {
            dish.setSpecialPrice(null);
            dish.setSpecialPriceStart(null);
            dish.setSpecialPriceEnd(null);
        }
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
