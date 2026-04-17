package com.example.ui.controller;

import com.example.entity.order.OrderInfo;
import com.example.entity.order.Orders;
import com.example.entity.restaurant.Dish;
import com.example.entity.restaurant.Restaurant;
import com.example.entity.user.Customer;
import com.example.entity.user.RestaurantOwner;
import com.example.entity.user.User;
import com.example.service.AdminService;
import com.example.service.RestaurantOrderService;
import com.example.service.RestaurantOwnerService;
import com.example.service.RestaurantService;
import com.example.session.UserSession;
import com.example.ui.helper.AlertWindow;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class AdminOrderController {
    private final AdminService adminService = new AdminService();
    private final RestaurantOrderService restaurantOrderService = new RestaurantOrderService();
    private final RestaurantService restaurantService = new RestaurantService();
    private final RestaurantOwnerService ownerService = new RestaurantOwnerService();

    @FXML
    private ComboBox<Customer> userCombo;
    @FXML
    private ComboBox<Restaurant> restaurantCombo;
    @FXML
    private TableView<Dish> menuTable;
    @FXML
    private ListView<String> basketListView;
    @FXML
    private TextField quantityField;
    @FXML
    private Label totalLabel;
    @FXML
    private TableColumn<Dish, String> colDishName;
    @FXML
    private TableColumn<Dish, Double> colDishPrice;

    private Orders currentOrder = new Orders();

    @FXML
    private void initialize() {
        User currentUser = UserSession.getCurrentUser();

        userCombo.setConverter(new javafx.util.StringConverter<Customer>() {
            @Override
            public String toString(Customer customer) {
                return (customer == null) ? "" : customer.getName() + " " + customer.getSurname();
            }

            @Override
            public Customer fromString(String string) {
                return null;
            }
        });

        restaurantCombo.setConverter(new javafx.util.StringConverter<Restaurant>() {
            @Override
            public String toString(Restaurant restaurant) {
                return (restaurant == null) ? "" : restaurant.getRestaurantName();
            }

            @Override
            public Restaurant fromString(String string) {
                return null;
            }
        });

        List<Customer> customers = adminService.allUsers(Customer.class);
        userCombo.setItems(FXCollections.observableArrayList(customers));

        List<Restaurant> restaurants;
        if (currentUser instanceof RestaurantOwner) {
            restaurants = ownerService.findAllRestaurants(currentUser.getUserId());
        } else {
            restaurants = adminService.getAllRestaurants();
        }
        restaurantCombo.setItems(FXCollections.observableArrayList(restaurants));

        restaurantCombo.setOnAction(e -> {
            Restaurant selected = restaurantCombo.getValue();
            if (selected != null) {
                menuTable.setItems(FXCollections.observableArrayList(
                        restaurantService.findAllDishes(selected.getRestaurantId())
                ));
                currentOrder = new Orders();
                updateBasketUI();
            }
        });

        colDishName.setCellValueFactory(new PropertyValueFactory<>("dishName"));
        colDishPrice.setCellValueFactory(cellData -> {
            double activePrice = cellData.getValue().getActivePrice();
            return new javafx.beans.property.SimpleObjectProperty<>(activePrice);
        });

        quantityField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("([1-9][0-9]*)?")) {
                return change;
            }
            return null;
        }));
    }

    @FXML
    private void handleAddToBasket() {
        Dish selectedDish = menuTable.getSelectionModel().getSelectedItem();
        String qtyText = quantityField.getText();

        if (selectedDish == null || qtyText.isEmpty()) {
            AlertWindow.showError("Selection Error", "Please select a dish and enter a valid quantity.");
            return;
        }

        int qty = Integer.parseInt(qtyText);
        if (qty <= 0) {
            AlertWindow.showError("Input Error", "Quantity must be greater than 0.");
            return;
        }

        OrderInfo item = new OrderInfo();
        item.setDish(selectedDish);
        item.setQuantity(qty);
        item.setPrice(selectedDish.getActivePrice());

        currentOrder.addOrderItem(item);
        updateBasketUI();
        quantityField.clear();
    }

    private void updateBasketUI() {
        basketListView.getItems().clear();
        for (OrderInfo item : currentOrder.getOrderItemList()) {
            basketListView.getItems().add(
                    item.getDish().getDishName() + " | x" + item.getQuantity() + " | " + (item.getPrice() * item.getQuantity()) + " EUR"
            );
        }
        totalLabel.setText("Total Sum: " + currentOrder.getOrderTotalPrice() + " EUR");
    }

    @FXML
    private void handleConfirmOrder() {
        if (userCombo.getValue() == null || currentOrder.getOrderItemList().isEmpty()) {
            AlertWindow.showError("Validation Error", "Customer must be selected and basket cannot be empty.");
            return;
        }

        currentOrder.setCustomer(userCombo.getValue());
        currentOrder.setRestaurant(restaurantCombo.getValue());
        currentOrder.setStatus("PENDING");

        try {
            restaurantOrderService.addOrder(currentOrder);
            AlertWindow.showInformation("Success", "Order created successfully!");
            currentOrder = new Orders();
            updateBasketUI();
        } catch (Exception e) {
            AlertWindow.showError("Database Error", "Failed to save the order.");
            e.printStackTrace();
        }
    }
}