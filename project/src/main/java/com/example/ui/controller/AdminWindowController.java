package com.example.ui.controller;

import com.example.ui.util.FadeAnimation;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class AdminWindowController {
    @FXML private Button customerButton;
    @FXML private Button driverButton;
    @FXML private Button restaurantButton;
    @FXML private Button ordersButton;
    @FXML private Button statisticsButton;
    @FXML private VBox customerTab;
    @FXML private VBox driverTab;
    @FXML private VBox restaurantTab;
    @FXML private VBox ordersTab;
    @FXML private VBox statisticsTab;

    @FXML private void switchToCustomers(){
        driverTab.setVisible(false);
        restaurantTab.setVisible(false);
        ordersTab.setVisible(false);
        statisticsTab.setVisible(false);
        customerTab.setVisible(true);
        FadeAnimation.fadeAnimation(customerTab);
        System.out.println("Switching to customer");
    }
    @FXML private void switchToDrivers(){
        customerTab.setVisible(false);
        restaurantTab.setVisible(false);
        ordersTab.setVisible(false);
        statisticsTab.setVisible(false);
        driverTab.setVisible(true);
        FadeAnimation.fadeAnimation(driverTab);
        System.out.println("Switching to driver");
    }
    @FXML private void switchToRestaurant(){
        customerTab.setVisible(false);
        driverTab.setVisible(false);
        ordersTab.setVisible(false);
        statisticsTab.setVisible(false);
        restaurantTab.setVisible(true);
        FadeAnimation.fadeAnimation(restaurantTab);
        System.out.println("Switching to restaurant");
    }
    @FXML private void switchToOrders(){
        customerTab.setVisible(false);
        driverTab.setVisible(false);
        restaurantTab.setVisible(false);
        statisticsTab.setVisible(false);
        ordersTab.setVisible(true);
        FadeAnimation.fadeAnimation(ordersTab);
        System.out.println("Switching to orders");
    }
    @FXML private void switchToStatistics(){
        customerTab.setVisible(false);
        driverTab.setVisible(false);
        restaurantTab.setVisible(false);
        ordersTab.setVisible(false);
        statisticsTab.setVisible(true);
        FadeAnimation.fadeAnimation(statisticsTab);
        System.out.println("Switching to statistics");
    }
}
