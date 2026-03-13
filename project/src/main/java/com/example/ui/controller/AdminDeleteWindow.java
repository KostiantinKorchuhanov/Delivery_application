package com.example.ui.controller;

import com.example.app.NavigationManager;
import com.example.service.AdminService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AdminDeleteWindow {
    @FXML private TextField emailField;
    @FXML private Button deleteButton;

//    @FXML private void initialize(){
//
//    }

    @FXML private void deleteUser(){
        AdminService adminService = new AdminService();
        adminService.deleteUser(emailField.getText());
        NavigationManager.closeCurrentStage(deleteButton);
    }

}
