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
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
    private TableColumn<Orders, String> orderPriceColumn;
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

        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        orderPriceColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("%.2f $", cellData.getValue().getOrderTotalPrice())));

        orderRestaurantColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRestaurant() != null ?
                        cellData.getValue().getRestaurant().getRestaurantName() : "N/A"));

        orderCustomerColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCustomer() != null ?
                        cellData.getValue().getCustomer().getName() : "N/A"));

        orderDriverColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDriver() != null ?
                        cellData.getValue().getDriver().getName() : "Not Assigned"));

        orderDetailsColumn.setCellValueFactory(cellData -> {
            Orders order = cellData.getValue();
            if (order != null && order.getOrderItemList() != null) {
                return new SimpleStringProperty(order.getOrderItemList().stream()
                        .map(item -> (item.getDish() != null ? item.getDish().getDishName() : "Unknown") + " x" + item.getQuantity())
                        .collect(Collectors.joining(", ")));
            }
            return new SimpleStringProperty("");
        });

        tableOfOrders.setEditable(true);

        orderStatusColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        orderStatusColumn.setOnEditCommit(e -> {
            e.getRowValue().setStatus(e.getNewValue());
            restaurantOrderService.updateOrder(e.getRowValue());
        });

        orderPriceColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        orderPriceColumn.setOnEditCommit(e -> {
            try {
                double p = Double.parseDouble(e.getNewValue().replace("$", "").trim());
                e.getRowValue().setOrderTotalPrice(p);
                restaurantOrderService.updateOrder(e.getRowValue());
            } catch (Exception ex) {
                AlertWindow.showError("Error", "Invalid price");
            }
            tableOfOrders.refresh();
        });

        orderRestaurantColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        orderRestaurantColumn.setOnEditCommit(e -> {
            if (e.getRowValue().getRestaurant() != null) {
                e.getRowValue().getRestaurant().setRestaurantName(e.getNewValue());
                restaurantOrderService.updateOrder(e.getRowValue());
            }
        });

        filteredOrders = new FilteredList<>(ordersMasterList, p -> true);
        tableOfOrders.setItems(filteredOrders);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (tableOfOrders.isVisible()) filterOrders(newValue);
            else searchUsers();
        });

        homeButton.fire();
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
            boolean matchesCustomer = order.getCustomer() != null &&
                    (order.getCustomer().getName().toLowerCase().contains(lower) ||
                            order.getCustomer().getSurname().toLowerCase().contains(lower));
            boolean matchesRestaurant = order.getRestaurant() != null &&
                    order.getRestaurant().getRestaurantName().toLowerCase().contains(lower);
            boolean matchesStatus = order.getStatus() != null &&
                    order.getStatus().toLowerCase().contains(lower);
            boolean matchesDriver = order.getDriver() != null &&
                    (order.getDriver().getName().toLowerCase().contains(lower) ||
                            order.getDriver().getSurname().toLowerCase().contains(lower));

            return matchesCustomer || matchesRestaurant || matchesStatus || matchesDriver;
        });
    }

    @FXML
    private void switchToHome() {
        searchField.setDisable(false);
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
        searchField.setDisable(false);
        addButton.setText("Add User");
        deleteUser = true;
    }

    @FXML
    private void switchToDrivers() {
        registerUser = true;
        buttonManagement(false, false, true, false, false, false, true);
        loadUsersByClass(Driver.class);
        searchField.setDisable(false);
        addButton.setText("Add User");
        deleteUser = true;
    }

    @FXML
    private void switchToRestaurant() {
        registerUser = true;
        buttonManagement(false, false, false, true, false, false, true);
        loadUsersByClass(RestaurantOwner.class);
        searchField.setDisable(false);
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
        searchField.setDisable(false);
        ordersMasterList.setAll(restaurantOrderService.getAllOrders());
        filterOrders(searchField.getText());
        deleteUser = false;

        tableOfOrders.setVisible(true);
        FadeAnimation.fadeAnimation(tableOfOrders);
    }

    @FXML
    private void switchToStatistics() {
        statisticsTab.setStyle("-fx-background-color: white; -fx-padding: 20;");
        buttonManagement(false, false, false, false, false, true, true);
        tableOfUsers.setVisible(false);
        tableOfOrders.setVisible(false);
        searchField.setDisable(true);

        statisticsTab.getChildren().clear();
        statisticsTab.setAlignment(Pos.CENTER);
        statisticsTab.setSpacing(20);
        statisticsTab.setVisible(true);

        Long comp = adminService.getOrderCountByStatus("COMPLETED");
        Long pend = adminService.getOrderCountByStatus("PENDING");
        Long deliv = adminService.getOrderCountByStatus("DELIVERED");

        long completedCount = (comp != null) ? comp : 0;
        long pendingCount = (pend != null) ? pend : 0;
        long deliveredCount = (deliv != null) ? deliv : 0;

        PieChart statusChart = new PieChart();
        statusChart.setTitle("Orders by Status");
        if (completedCount > 0) statusChart.getData().add(new PieChart.Data("Completed", completedCount));
        if (pendingCount > 0) statusChart.getData().add(new PieChart.Data("Pending", pendingCount));
        if (deliveredCount > 0) statusChart.getData().add(new PieChart.Data("Delivered", deliveredCount));

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of Orders");
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Orders in Last 7 Days");
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Orders Count");

        LocalDate weekAgo = LocalDate.now().minusDays(7);
        List<Orders> allOrders = restaurantOrderService.getAllOrders();

        Map<LocalDate, Long> ordersByDate = allOrders.stream()
                .filter(o -> o.getCreatedAt() != null && o.getCreatedAt().toLocalDate().isAfter(weekAgo))
                .collect(Collectors.groupingBy(
                        o -> o.getCreatedAt().toLocalDate(),
                        TreeMap::new,
                        Collectors.counting()
                ));

        ordersByDate.forEach((date, count) -> {
            series.getData().add(new XYChart.Data<>(date.toString(), count));
        });
        lineChart.getData().add(series);
        Double rev = adminService.getTotalRevenue();
        double totalRevenue = (rev != null) ? rev : 0.0;
        VBox revenueBox = new VBox(10);
        revenueBox.setAlignment(Pos.CENTER);
        revenueBox.setStyle("-fx-background-color: rgba(121, 160, 195, 0.1); -fx-padding: 20; -fx-border-radius: 15; -fx-border-color: #79a0c3;");
        javafx.scene.control.Label revValue = new javafx.scene.control.Label(String.format("Total Revenue: %.2f $", totalRevenue));
        revValue.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        revenueBox.getChildren().add(revValue);
        statisticsTab.getChildren().addAll(statusChart, lineChart, revenueBox);
        FadeAnimation.fadeAnimation(statisticsTab);
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
