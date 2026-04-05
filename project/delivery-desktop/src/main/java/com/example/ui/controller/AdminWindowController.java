package com.example.ui.controller;

import com.example.app.NavigationManager;
import com.example.entity.order.Orders;
import com.example.entity.user.Customer;
import com.example.entity.user.Driver;
import com.example.entity.user.RestaurantOwner;
import com.example.entity.user.User;
import com.example.service.AdminService;
import com.example.service.RestaurantOrderService;
import com.example.session.UserSession;
import com.example.ui.helper.AlertWindow;
import com.example.ui.util.FadeAnimation;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AdminWindowController {
    private final AdminService adminService = new AdminService();
    private final RestaurantOrderService restaurantOrderService = new RestaurantOrderService();
    private final ObservableList<Orders> ordersMasterList = FXCollections.observableArrayList();
    private boolean registerUser = true;
    private boolean deleteUser = true;
    @FXML
    private Button customerButton;
    @FXML
    private Button driverButton;
    @FXML
    private Button restaurantButton;
    @FXML
    private Button ordersButton;
    @FXML
    private Button statisticsButton;
    @FXML
    private Button homeButton;
    @FXML
    private TextField searchField;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button logoutButton;
    @FXML
    private VBox ordersTab;
    @FXML
    private VBox statisticsTab;
    @FXML
    private TableView<User> tableOfUsers;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, String> surnameColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, String> phoneColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableView<Orders> tableOfOrders;
    @FXML
    private TableColumn<Orders, String> orderCustomerColumn;
    @FXML
    private TableColumn<Orders, String> orderRestaurantColumn;
    @FXML
    private TableColumn<Orders, String> orderDetailsColumn;
    @FXML
    private TableColumn<Orders, Double> orderPriceColumn;
    @FXML
    private TableColumn<Orders, String> orderStatusColumn;
    @FXML
    private TableColumn<Orders, String> orderDriverColumn;
    private FilteredList<Orders> filteredOrders;

    @FXML
    private void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        setupUserTableEditing();

        orderPriceColumn.setCellValueFactory(new PropertyValueFactory<>("orderTotalPrice"));
        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        orderCustomerColumn.setCellValueFactory(d -> {
            User c = d.getValue().getCustomer();
            return new SimpleStringProperty(c.getName() + " " + c.getSurname());
        });
        orderRestaurantColumn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getRestaurant().getRestaurantName()));
        orderDetailsColumn.setCellValueFactory(d -> {
            String summary = d.getValue().getOrderItemList().stream().map(item -> item.getDish().getDishName() + " x" + item.getQuantity()).collect(Collectors.joining(", "));
            return new SimpleStringProperty(summary);
        });
        orderDriverColumn.setCellValueFactory(cellData -> {
            User driver = cellData.getValue().getDriver();
            if (driver != null) {
                return new SimpleStringProperty(driver.getName() + " " + driver.getSurname());
            } else {
                return new SimpleStringProperty("Not Assigned");
            }
        });

        filteredOrders = new FilteredList<>(ordersMasterList, p -> true);
        tableOfOrders.setItems(filteredOrders);
        tableOfOrders.setEditable(true);

        orderStatusColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        orderStatusColumn.setOnEditCommit(event -> {
            Orders order = event.getRowValue();
            order.setStatus(event.getNewValue());
            restaurantOrderService.updateOrder(order);
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (tableOfOrders.isVisible()) {
                filterOrders(newValue);
            } else {
                if (newValue != null && !newValue.isEmpty()) {
                    searchUsers();
                }
            }
        });
    }

    private void setupUserTableEditing() {
        tableOfUsers.setEditable(true);
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        surnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        usernameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        nameColumn.setOnEditCommit(event -> {
            event.getRowValue().setName(event.getNewValue());
            adminService.updateUser(event.getRowValue());
        });
        surnameColumn.setOnEditCommit(event -> {
            event.getRowValue().setSurname(event.getNewValue());
            adminService.updateUser(event.getRowValue());
        });
        emailColumn.setOnEditCommit(event -> {
            event.getRowValue().setEmail(event.getNewValue());
            adminService.updateUser(event.getRowValue());
        });
        phoneColumn.setOnEditCommit(event -> {
            event.getRowValue().setPhoneNumber(event.getNewValue());
            adminService.updateUser(event.getRowValue());
        });
        usernameColumn.setOnEditCommit(event -> {
            event.getRowValue().setUsername(event.getNewValue());
            adminService.updateUser(event.getRowValue());
        });
    }

    private void filterOrders(String searchText) {
        filteredOrders.setPredicate(order -> {
            if (searchText == null || searchText.isEmpty()) return true;
            String lower = searchText.toLowerCase();
            boolean matchesCustomer = order.getCustomer().getName().toLowerCase().contains(lower) || order.getCustomer().getSurname().toLowerCase().contains(lower);
            boolean matchesRestaurant = order.getRestaurant().getRestaurantName().toLowerCase().contains(lower);
            boolean matchesStatus = order.getStatus().toLowerCase().contains(lower);
            boolean matchesDriver = false;
            if (order.getDriver() != null) {
                matchesDriver = order.getDriver().getName().toLowerCase().contains(lower) || order.getDriver().getSurname().toLowerCase().contains(lower);
            }
            return matchesCustomer || matchesRestaurant || matchesStatus || matchesDriver;
        });
    }

    @FXML
    private void switchToHome() {
        registerUser = true;
        buttonManagement(true, false, false, false, false, false, false);
        searchUsers();
        FadeAnimation.fadeAnimation(tableOfUsers);
        addButton.setText("Add User");
        deleteUser = true;
    }

    @FXML
    private void searchUsers() {
        List<User> result = adminService.searchUsers(searchField.getText());
        tableOfUsers.setItems(FXCollections.observableArrayList(result));
        tableOfUsers.setVisible(true);
        tableOfOrders.setVisible(false);
        addButton.setDisable(false);
    }

    @FXML
    private void switchToCustomers() {
        registerUser = true;
        buttonManagement(false, true, false, false, false, false, true);
        loadUsersByClass(Customer.class);
        addButton.setText("Add User");
        deleteUser = true;
    }

    @FXML
    private void switchToDrivers() {
        registerUser = true;
        buttonManagement(false, false, true, false, false, false, true);
        loadUsersByClass(Driver.class);
        addButton.setText("Add User");
        deleteUser = true;
    }

    @FXML
    private void switchToRestaurant() {
        registerUser = true;
        buttonManagement(false, false, false, true, false, false, true);
        loadUsersByClass(RestaurantOwner.class);
        addButton.setText("Add User");
        deleteUser = true;
    }

    private void loadUsersByClass(Class<? extends User> clazz) {
        FadeAnimation.fadeAnimation(tableOfUsers);
        tableOfUsers.setItems(FXCollections.observableArrayList(adminService.allUsers(clazz)));
        tableOfUsers.setVisible(true);
        tableOfOrders.setVisible(false);
        addButton.setText("Add User");
    }

    @FXML
    private void switchToOrders() {
        registerUser = false;
        buttonManagement(false, false, false, false, true, false, false);
        addButton.setText("Add Order");
        tableOfUsers.setVisible(false);
        statisticsTab.setVisible(false);
        ordersMasterList.setAll(restaurantOrderService.getAllOrders());
        filterOrders(searchField.getText());
        deleteUser = false;

        tableOfOrders.setVisible(true);
        FadeAnimation.fadeAnimation(tableOfOrders);
    }

    @FXML
    private void switchToStatistics() {
        buttonManagement(false, false, false, false, false, true, true);
        FadeAnimation.fadeAnimation(statisticsTab);
        addButton.setDisable(true);
        String video = getClass().getResource("/rickroll.mp4").toExternalForm();
        Media media = new Media(video);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(600);
        mediaView.setPreserveRatio(true);
        statisticsTab.getChildren().clear();
        statisticsTab.getChildren().add(mediaView);
        statisticsTab.setAlignment(Pos.CENTER);
        mediaPlayer.play();
    }

    private void buttonManagement(boolean home, boolean customer, boolean driver, boolean restaurant, boolean orders, boolean statistics, boolean clearField) {
        homeButton.setDisable(home);
        customerButton.setDisable(customer);
        driverButton.setDisable(driver);
        restaurantButton.setDisable(restaurant);
        ordersButton.setDisable(orders);
        statisticsButton.setDisable(statistics);
        ordersTab.setVisible(orders);
        statisticsTab.setVisible(statistics);
        if (clearField) searchField.clear();
    }

    @FXML
    private void openRegistration() throws IOException {
        if (registerUser) {
            NavigationManager.navigateToRegistration();
        } else {
            NavigationManager.navigateToOrderCreation();
        }
    }

    @FXML
    private void openDeletion() throws IOException {
        if (deleteUser) {
            NavigationManager.navigateToDelete();
        } else {
            handleDeleteOrder();
        }
    }

    @FXML
    private void logoutUser() throws IOException {
        UserSession.logout();
        NavigationManager.closeCurrentStage(logoutButton);
        NavigationManager.navigateToLogout();
    }

    @FXML
    private void handleDeleteOrder() {
        Orders selectedOrder = tableOfOrders.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            AlertWindow.showError("Selection Error", "Please select an order to delete.");
            return;
        }
        boolean confirm = AlertWindow.showConfirmation("Delete Order", "Are you sure you want to delete Order #" + selectedOrder.getOrderId() + "?");
        if (confirm) {
            try {
                restaurantOrderService.deleteOrder(selectedOrder);
                ordersMasterList.remove(selectedOrder);
                AlertWindow.showInformation("Success", "Order deleted successfully.");
            } catch (Exception e) {
                AlertWindow.showError("Database Error", "Could not delete order.");
            }
        }
    }

    @FXML
    private void changeMenuVisibility() {
    }
}
