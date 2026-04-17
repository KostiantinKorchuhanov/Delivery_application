package com.example.ui.controller;

import com.example.app.NavigationManager;
import com.example.entity.order.Orders;
import com.example.entity.restaurant.Restaurant;
import com.example.entity.restaurant.RestaurantReview;
import com.example.entity.user.RestaurantOwner;
import com.example.entity.user.User;
import com.example.service.RestaurantOrderService;
import com.example.service.RestaurantOwnerService;
import com.example.session.UserSession;
import com.example.ui.helper.AlertWindow;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.converter.DoubleStringConverter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantOwnerController {
    private final RestaurantOrderService restaurantOrderService = new RestaurantOrderService();
    private final RestaurantOwnerService restaurantOwnerService = new RestaurantOwnerService();
    private final ObservableList<Orders> ordersData = FXCollections.observableArrayList();
    @FXML
    private FlowPane restaurantFlowPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Button myRestaurantsButton;
    @FXML
    private Button logoutButton;
    @FXML
    private TextField searchField;
    @FXML
    private Button orderButton;
    @FXML
    private TableView<Orders> tableOrders;
    @FXML
    private TableColumn<Orders, Integer> colId;
    @FXML
    private TableColumn<Orders, String> colRestaurant;
    @FXML
    private TableColumn<Orders, String> colCustomer;
    @FXML
    private TableColumn<Orders, String> colItems;
    @FXML
    private TableColumn<Orders, Double> colTotal;
    @FXML
    private TableColumn<Orders, String> colStatus;
    @FXML
    private TableColumn<Orders, String> colDriver;
    @FXML
    private VBox restaurantsContainer;
    @FXML
    private ScrollPane statisticsScrollPane;
    @FXML
    private ComboBox<Restaurant> restaurantSelector;
    @FXML
    private Label totalProfitLabel;
    private FilteredList<Orders> filteredOrders;
    private int currentOwnerId;
    private boolean restaurantTab = true;

    @FXML
    private void initialize() {
        User user = UserSession.getCurrentUser();
        if (user instanceof RestaurantOwner) {
            currentOwnerId = user.getUserId();
        } else {
            AlertWindow.showError("Session Error", "User session not found.");
            return;
        }
        colId.setCellValueFactory(new PropertyValueFactory<>("orderId"));

        colDriver.setCellValueFactory(cellData -> {
            User driver = cellData.getValue().getDriver();
            return new SimpleStringProperty(driver != null ?
                    driver.getName() + " " + driver.getSurname() : "Not Assigned");
        });

        colTotal.setCellValueFactory(new PropertyValueFactory<>("orderTotalPrice"));
        colTotal.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colTotal.setOnEditCommit(event -> {
            Orders order = event.getRowValue();
            order.setOrderTotalPrice(event.getNewValue());
            restaurantOrderService.updateOrder(order);
        });

        colRestaurant.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRestaurant().getRestaurantName()));
        colRestaurant.setCellFactory(TextFieldTableCell.forTableColumn());
        colRestaurant.setOnEditCommit(event -> {
            Orders order = event.getRowValue();
            order.getRestaurant().setRestaurantName(event.getNewValue());
            restaurantOrderService.updateOrder(order);
        });
        setupStatusEditing();

        colCustomer.setCellValueFactory(cellData -> {
            User c = cellData.getValue().getCustomer();
            return new SimpleStringProperty(c.getName() + " " + c.getSurname() + " (" + c.getPhoneNumber() + ")");
        });

        colItems.setCellValueFactory(cellData -> {
            String items = cellData.getValue().getOrderItemList().stream()
                    .map(item -> (item.getDish() != null ? item.getDish().getDishName() : "Unknown") + " x" + item.getQuantity())
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(items);
        });

        filteredOrders = new FilteredList<>(ordersData, p -> true);
        tableOrders.setItems(filteredOrders);
        tableOrders.setEditable(true);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (scrollPane.isVisible()) {
                updateRestaurantList(newVal);
            } else if (tableOrders.isVisible()) {
                filterOrders(newVal);
            }
        });

        setupStatisticsLogic();
        openRestaurants();
    }

    private void setupStatisticsLogic() {
        restaurantSelector.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Restaurant r) {
                return r == null ? "" : r.getRestaurantName();
            }

            @Override
            public Restaurant fromString(String s) {
                return null;
            }
        });
        restaurantSelector.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                calculateProfitForRestaurant(newVal);
            }
        });
    }

    private void calculateProfitForRestaurant(Restaurant restaurant) {
        List<Orders> allOrders = restaurantOrderService.getAllOrdersForOwner(currentOwnerId);
        double total = allOrders.stream()
                .filter(o -> o.getRestaurant().getRestaurantId() == restaurant.getRestaurantId())
                .filter(o -> "COMPLETED".equals(o.getStatus()))
                .mapToDouble(Orders::getOrderTotalPrice)
                .sum();
        if (totalProfitLabel != null) {
            totalProfitLabel.setText(String.format("%.2f $", total));
        }
    }

    private void setupStatusEditing() {
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colStatus.setCellFactory(ComboBoxTableCell.forTableColumn(
                "PENDING", "PREPARING", "READY_FOR_PICKUP", "DELIVERING", "COMPLETED", "CANCELLED"
        ));
        colStatus.setOnEditCommit(event -> {
            Orders order = event.getRowValue();
            order.setStatus(event.getNewValue());
            restaurantOrderService.updateOrder(order);
            if (statisticsScrollPane.isVisible()) {
                calculateProfitForRestaurant(restaurantSelector.getValue());
            }
        });
    }

    private void filterOrders(String searchText) {
        filteredOrders.setPredicate(order -> {
            if (searchText == null || searchText.isEmpty()) return true;
            String lower = searchText.toLowerCase();
            return order.getRestaurant().getRestaurantName().toLowerCase().contains(lower) ||
                    order.getCustomer().getName().toLowerCase().contains(lower) ||
                    order.getCustomer().getSurname().toLowerCase().contains(lower) ||
                    order.getStatus().toLowerCase().contains(lower) ||
                    String.valueOf(order.getOrderId()).contains(lower);
        });
    }

    @FXML
    private void openOrders() {
        statisticsScrollPane.setVisible(false);
        tableOrders.setVisible(true);
        scrollPane.setVisible(false);
        myRestaurantsButton.setDisable(false);
        orderButton.setDisable(true);
        searchField.clear();
        loadData();
        restaurantTab = false;
    }

    private void loadData() {
        ordersData.setAll(restaurantOrderService.getAllOrdersForOwner(currentOwnerId));
    }

    private void updateRestaurantList(String searchText) {
        try {
            List<Restaurant> found = restaurantOwnerService.searchRestaurants(currentOwnerId, searchText);
            renderRestaurants(found);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openRestaurants() {
        statisticsScrollPane.setVisible(false);
        tableOrders.setVisible(false);
        scrollPane.setVisible(true);
        myRestaurantsButton.setDisable(true);
        orderButton.setDisable(false);
        searchField.clear();
        renderRestaurants(restaurantOwnerService.findAllRestaurants(currentOwnerId));
        restaurantTab = true;
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

    @FXML
    private void addRestaurant() throws IOException {
        if (restaurantTab) {
            NavigationManager.navigateToNewRestaurant();
            openRestaurants();
        } else {
            NavigationManager.navigateToOrderCreation();
        }
    }

    @FXML
    private void openStatistics() {
        tableOrders.setVisible(false);
        scrollPane.setVisible(false);
        statisticsScrollPane.setVisible(true);
        myRestaurantsButton.setDisable(false);
        orderButton.setDisable(false);
        restaurantsContainer.getChildren().clear();
        List<Restaurant> myRestaurants = restaurantOwnerService.findAllRestaurants(currentOwnerId);
        restaurantSelector.setItems(FXCollections.observableArrayList(myRestaurants));
        if (!myRestaurants.isEmpty()) {
            if (restaurantSelector.getSelectionModel().getSelectedItem() == null) {
                restaurantSelector.getSelectionModel().selectFirst();
            }
            calculateProfitForRestaurant(restaurantSelector.getSelectionModel().getSelectedItem());
        }
        for (Restaurant res : myRestaurants) {
            VBox restaurantBlock = new VBox(10);
            restaurantBlock.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-border-color: #eee; -fx-border-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");
            Label nameLabel = new Label(res.getRestaurantName());
            nameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
            Label ratingLabel = new Label(String.format("Average Rating: %.1f ⭐", res.getAverageRating()));
            ratingLabel.setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
            VBox reviewsList = new VBox(10);
            reviewsList.setStyle("-fx-padding: 10; -fx-background-color: #fafafa;");
            if (res.getReviews() != null && !res.getReviews().isEmpty()) {
                for (RestaurantReview review : res.getReviews()) {
                    VBox reviewItem = new VBox(5);
                    reviewItem.setStyle("-fx-padding: 10; -fx-background-color: white; -fx-border-radius: 5; -fx-border-color: #f0f0f0;");
                    Label userInfo = new Label(review.getUser().getName() + " " + review.getUser().getSurname());
                    userInfo.setStyle("-fx-font-weight: bold;");
                    Label stars = new Label("⭐".repeat(review.getRating()));
                    stars.setStyle("-fx-text-fill: #f1c40f;");
                    Label comment = new Label(review.getComment());
                    comment.setWrapText(true);
                    comment.setStyle("-fx-text-fill: #7f8c8d;");
                    reviewItem.getChildren().addAll(userInfo, stars, comment);
                    reviewsList.getChildren().add(reviewItem);
                }
            } else {
                reviewsList.getChildren().add(new Label("No reviews for this restaurant yet."));
            }
            TitledPane titledPane = new TitledPane("Customer Reviews (" + (res.getReviews() != null ? res.getReviews().size() : 0) + ")", reviewsList);
            titledPane.setExpanded(false);
            titledPane.setAnimated(true);
            restaurantBlock.getChildren().addAll(nameLabel, ratingLabel, titledPane);
            restaurantsContainer.getChildren().add(restaurantBlock);
        }
    }

    @FXML
    public void logoutUser(ActionEvent actionEvent) throws IOException {
        UserSession.logout();
        NavigationManager.closeCurrentStage(logoutButton);
        NavigationManager.navigateToLogout();
    }
}