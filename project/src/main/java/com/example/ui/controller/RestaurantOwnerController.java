package com.example.ui.controller;

import com.example.app.NavigationManager;
import com.example.entity.order.Orders;
import com.example.entity.restaurant.Restaurant;
import com.example.entity.user.RestaurantOwner;
import com.example.entity.user.User;
import com.example.service.RestaurantOrderService;
import com.example.service.RestaurantOwnerService;
import com.example.session.UserSession;
import com.example.ui.helper.AlertWindow;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantOwnerController {
    @FXML private FlowPane restaurantFlowPane;
    @FXML private ScrollPane scrollPane;
    @FXML private Button myRestaurantsButton;
    @FXML private TextField searchField;
    @FXML private TableView<Orders> tableOrders;
    @FXML private TableColumn<Orders, Integer> colId;
    @FXML private TableColumn<Orders, String> colRestaurant;
    @FXML private TableColumn<Orders, String> colCustomer;
    @FXML private TableColumn<Orders, String> colItems;
    @FXML private TableColumn<Orders, Double> colTotal;
    @FXML private TableColumn<Orders, String> colStatus;

    private final RestaurantOrderService restaurantOrderService = new RestaurantOrderService();
    private ObservableList<Orders> ordersData = FXCollections.observableArrayList();
    private final RestaurantOwnerService restaurantOwnerService = new RestaurantOwnerService();
    private int currentOwnerId;

    @FXML
    private void initialize() {
        User user = UserSession.getCurrentUser();
        if (user instanceof RestaurantOwner) {
            currentOwnerId = user.getUserId();
        } else {
            AlertWindow.showError("Session Error", "User session not found or you are not an owner.");
            return;
        }

        colId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("orderTotalPrice"));
        colRestaurant.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRestaurant().getRestaurantName()));
        colCustomer.setCellValueFactory(cellData -> {
            User c = cellData.getValue().getCustomer();
            return new SimpleStringProperty(c.getName() + " " + c.getSurname() + " (" + c.getPhoneNumber() + ")");
        });
        colItems.setCellValueFactory(cellData -> {
            String items = cellData.getValue().getOrderItemList().stream()
                    .map(item -> item.getDish().getDishName() + " x" + item.getQuantity())
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(items);
        });

        setupStatusEditing();
        tableOrders.setEditable(true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateRestaurantList(newValue);
        });
        openRestaurants();
    }

    private void setupStatusEditing() {
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colStatus.setCellFactory(ComboBoxTableCell.forTableColumn(
                "PENDING", "PREPARING", "READY_FOR_PICKUP", "DELIVERING", "COMPLETED", "CANCELLED"
        ));
        colStatus.setOnEditCommit(event -> {
            Orders order = event.getRowValue();
            String newStatus = event.getNewValue();
            restaurantOrderService.updateStatus(order.getOrderId(), newStatus);
            order.setStatus(newStatus);
        });
    }

    @FXML
    private void openOrders(){
        tableOrders.setVisible(true);
        scrollPane.setVisible(false);
        myRestaurantsButton.setDisable(false);
        loadData();
    }

    private void loadData() {
        ordersData.setAll(restaurantOrderService.getAllOrdersForOwner(currentOwnerId));
        tableOrders.setItems(ordersData);
    }

    private void updateRestaurantList(String searchText) {
        try {
            List<Restaurant> found = restaurantOwnerService.searchRestaurants(currentOwnerId, searchText);
            renderRestaurants(found);
        } catch (Exception e) {
            AlertWindow.showError("Database Error", "Failed to search restaurants. Please check your connection.");
            e.printStackTrace();
        }
    }

    @FXML
    private void openRestaurants() {
        tableOrders.setVisible(false);
        scrollPane.setVisible(true);
        List<Restaurant> restaurantList = restaurantOwnerService.findAllRestaurants(currentOwnerId);
        renderRestaurants(restaurantList);
        myRestaurantsButton.setDisable(true);
    }



    private void renderRestaurants(List<Restaurant> restaurants) {
        restaurantFlowPane.getChildren().clear();
        for (Restaurant res : restaurants) {
            restaurantFlowPane.getChildren().add(createRestaurantCard(res));
        }
    }

    private VBox createRestaurantCard(Restaurant res) {
        VBox card = new VBox(10);
        card.getStyleClass().add("restaurant-card");
        card.setOnMouseClicked(event -> {
            try {
                NavigationManager.navigateToEditRestaurant(res, this::openRestaurants);
            } catch (IOException e) {
                AlertWindow.showError("Navigation Error", "Could not open the editor window.");
                e.printStackTrace();
            }
        });
        Text name = new Text(res.getRestaurantName());
        name.getStyleClass().add("restaurant-name");
        Text description = new Text(res.getDescription());
        description.getStyleClass().add("restaurant-description");
        description.setWrappingWidth(300);
        Text address = new Text(res.getAddress());
        address.getStyleClass().add("restaurant-address");
        card.getChildren().addAll(name, description, address);
        return card;
    }

    @FXML private void addRestaurant() throws IOException {
        NavigationManager.navigateToNewRestaurant();
        openRestaurants();
    }

    @FXML private void openStatistics(){
    }
}