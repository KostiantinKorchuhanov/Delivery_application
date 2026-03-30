package com.example.ui.controller;

import com.example.app.NavigationManager;
import com.example.entity.restaurant.Dish;
import com.example.entity.restaurant.Restaurant;
import com.example.entity.user.RestaurantOwner;
import com.example.service.RestaurantOwnerService;
import com.example.service.RestaurantService;
import com.example.session.UserSession;
import com.example.ui.helper.AlertWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;

public class RestaurantOwnerRegisterController {
    @FXML
    private TextField restaurantNameField;
    @FXML
    private TextField restaurantAddressField;
    @FXML
    private TextArea restaurantDescriptionField;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private AnchorPane foodPane;
    @FXML
    private Button discardButton;
    @FXML
    private FlowPane cardPane;
    @FXML
    private TextField searchField;

    private Restaurant editRestaurant;
    private Runnable runnable;
    private final RestaurantService restaurantService = new RestaurantService();

    @FXML
    private void initialize() {
        deleteButton.setDisable(true);
        toggleFoodPane(false);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            handleSearch(newValue);
        });
    }

    private void handleSearch(String text) {
        List<Dish> filteredDishes = restaurantService.searchDishes(editRestaurant.getRestaurantId(), text);

        cardPane.getChildren().clear();
        for (Dish dish : filteredDishes) {
            cardPane.getChildren().add(createDishCard(dish));
        }
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
        toggleFoodPane(true);
        loadFood();
    }

    @FXML
    private void addNewRestaurant() {
        if (editRestaurant == null) {
            deleteButton.setDisable(true);
            RestaurantOwner restaurantOwner = (RestaurantOwner) UserSession.getCurrentUser();
            String name = restaurantNameField.getText();
            String address = restaurantAddressField.getText();
            String description = restaurantDescriptionField.getText();
            RestaurantOwnerService restaurantOwnerService = new RestaurantOwnerService();
            restaurantOwnerService.saveNewRestaurant(name, description, address, restaurantOwner.getUserId());
        } else {
            deleteButton.setDisable(false);
            deleteButton.setOnAction(event -> {
                deleteRestaurant();
            });
            editRestaurant.setRestaurantName(restaurantNameField.getText());
            editRestaurant.setDescription(restaurantDescriptionField.getText());
            editRestaurant.setAddress(restaurantAddressField.getText());
            new RestaurantOwnerService().updateRestaurant(editRestaurant);
        }
        if (runnable != null) {
            runnable.run();
        }
        finishWork();
        NavigationManager.closeCurrentStage(addButton);
    }

    @FXML
    private void deleteRestaurant() {
        if (editRestaurant != null) {
            boolean confirmed = AlertWindow.showConfirmation(
                    "Delete Restaurant",
                    "Are you sure you want to delete " + editRestaurant.getRestaurantName() + "? This action cannot be undone."
            );
            if (confirmed) {
                new RestaurantOwnerService().deleteRestaurant(editRestaurant.getRestaurantId());
                finishWork();
                NavigationManager.closeCurrentStage(deleteButton);
                AlertWindow.showInformation("Success", "Restaurant deleted successfully.");
            }
        }
    }

    private void finishWork() {
        if (runnable != null) {
            runnable.run();
        }
    }

    private void toggleFoodPane(boolean show) {
        foodPane.setVisible(show);
        foodPane.setManaged(show);
        discardButton.setDisable(show);
        discardButton.setManaged(show);
    }

    private void loadFood() {
        if (editRestaurant == null) {
            return;
        }
        cardPane.getChildren().clear();
        cardPane.setHgap(20);
        cardPane.setVgap(20);
        cardPane.setPadding(new javafx.geometry.Insets(10));

        RestaurantService service = new RestaurantService();
        List<Dish> dishes = service.findAllDishes(editRestaurant.getRestaurantId());

        for (Dish dish : dishes) {
            cardPane.getChildren().add(createDishCard(dish));
        }
    }

    private VBox createDishCard(Dish dish) {
        VBox card = new VBox(5);
        card.getStyleClass().add("dish-card");
        Text name = new Text(dish.getDishName());
        name.getStyleClass().add("dish-name");
        Text description = new Text(dish.getDescription());
        description.getStyleClass().add("dish-description");
        Text price = new Text(dish.getPrice() + " Eurooo");
        price.getStyleClass().add("dish-price");
        card.getChildren().addAll(name, description, price);
        card.setOnMouseClicked(event -> {
            try {
                NavigationManager.navigateToEditDish(dish);
                loadFood();
            } catch (IOException e) {
                AlertWindow.showError("Navigation Error", "Could not open dish editor, idk why");
                e.printStackTrace();
            }
        });
        return card;
    }

    public void searchFood(ActionEvent actionEvent) {
    }

    public void addFood(ActionEvent actionEvent) throws IOException {
        NavigationManager.navigateToDish(editRestaurant);
        loadFood();
    }

    public void discardChanges(ActionEvent actionEvent) {
    }
}
