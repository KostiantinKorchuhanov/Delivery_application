package com.example.ui.controller;

import com.example.app.NavigationManager;
import com.example.entity.user.Customer;
import com.example.entity.user.Driver;
import com.example.entity.user.RestaurantOwner;
import com.example.entity.user.User;
import com.example.service.AdminService;
import com.example.ui.util.FadeAnimation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class AdminWindowController {
    @FXML private Button customerButton;
    @FXML private Button driverButton;
    @FXML private Button restaurantButton;
    @FXML private Button ordersButton;
    @FXML private Button statisticsButton;
    @FXML private Button homeButton;
    @FXML private TextField searchField;
    @FXML private Button addButton;
    @FXML private Button deleteButton;
    @FXML private VBox ordersTab;
    @FXML private VBox statisticsTab;
    @FXML private TableView<User> tableOfUsers;
    @FXML private TableColumn<User, String> nameColumn;
    @FXML private TableColumn<User, String> surnameColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, String> phoneColumn;
    @FXML private TableColumn<User, String> usernameColumn;
    private final AdminService adminService = new AdminService();

    @FXML private void initialize(){
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        surnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        usernameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        tableOfUsers.setEditable(true);

        nameColumn.setOnEditCommit(
                event -> {
                    User user = event.getRowValue();
                    user.setName(event.getNewValue());
                    adminService.updateUser(user);
                }
        );
        usernameColumn.setOnEditCommit(
                event -> {
                    User user = event.getRowValue();
                    user.setUsername(event.getNewValue());
                    adminService.updateUser(user);
                }
        );
        emailColumn.setOnEditCommit(
                event -> {
                    User user = event.getRowValue();
                    user.setEmail(event.getNewValue());
                    adminService.updateUser(user);
                }
        );
        phoneColumn.setOnEditCommit(
                event -> {
                    User user = event.getRowValue();
                    user.setPhoneNumber(event.getNewValue());
                    adminService.updateUser(user);
                }
        );
        surnameColumn.setOnEditCommit(
                event -> {
                    User user = event.getRowValue();
                    user.setSurname(event.getNewValue());
                    adminService.updateUser(user);
                }
        );
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null || !newValue.isEmpty()) {
                switchToHome();
            }
        });
    }

    @FXML private void switchToHome(){
        buttonManagement(true, false, false,false,false,false, false);
        FadeAnimation.fadeAnimation(tableOfUsers);
        List<User> result = adminService.searchUsers(searchField.getText());
        ObservableList<User> observableList = FXCollections.observableArrayList(result);
        tableOfUsers.setItems(observableList);
    }

    @FXML private void searchUsers(){
        List<User> result = adminService.searchUsers(searchField.getText());
        ObservableList<User> observableList = FXCollections.observableArrayList(result);
        tableOfUsers.setItems(observableList);
    }

    @FXML private void switchToCustomers(){
        buttonManagement(false, true, false,false,false,false, false);
        FadeAnimation.fadeAnimation(tableOfUsers);
        List<? extends User> customers = adminService.allUsers(Customer.class);
        ObservableList<User> obsList = FXCollections.observableArrayList(customers);
        tableOfUsers.setItems(obsList);
    }
    @FXML private void switchToDrivers(){
        buttonManagement(false, false, true,false,false,false, false);
        FadeAnimation.fadeAnimation(tableOfUsers);
        List<? extends User> driver = adminService.allUsers(Driver.class);
        ObservableList<User> obsList = FXCollections.observableArrayList(driver);
        tableOfUsers.setItems(obsList);
    }
    @FXML private void switchToRestaurant(){
        buttonManagement(false, false, false,true,false,false, false);
        FadeAnimation.fadeAnimation(tableOfUsers);
        List<? extends User> restaurant = adminService.allUsers(RestaurantOwner.class);
        ObservableList<User> obsList = FXCollections.observableArrayList(restaurant);
        tableOfUsers.setItems(obsList);
    }

    @FXML private void switchToOrders(){
        buttonManagement(false, false, false,false,true,false, false);
        FadeAnimation.fadeAnimation(ordersTab);
    }

    @FXML private void switchToStatistics(){
        buttonManagement(false, false, false,false,false,true, false);
        FadeAnimation.fadeAnimation(statisticsTab);
    }

    @FXML private void openRegistration() throws IOException {
        NavigationManager.navigateToRegistration();
    }

    @FXML private void openDeletion() throws IOException {
        NavigationManager.navigateToDelete();
    }

    private void buttonManagement(boolean home, boolean customer, boolean driver, boolean restaurant, boolean orders, boolean statistics, boolean clearField) {
        customerButton.setDisable(customer);
        driverButton.setDisable(driver);
        restaurantButton.setDisable(restaurant);
        ordersButton.setDisable(orders);
        statisticsButton.setDisable(statistics);
        homeButton.setDisable(home);
        if (clearField) {
            searchField.clear();
        }
    }

    @FXML private void changeMenuVisibility(){

    }
}
