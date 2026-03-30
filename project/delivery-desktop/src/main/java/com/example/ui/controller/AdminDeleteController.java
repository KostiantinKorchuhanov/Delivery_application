package com.example.ui.controller;

import com.example.app.NavigationManager;
import com.example.service.AdminService;
import com.example.ui.helper.AlertWindow;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AdminDeleteController {
    @FXML
    private TextField emailField;
    @FXML
    private Button deleteButton;

    @FXML
    private void deleteUser() {
        String email = emailField.getText();

        if (email == null || email.trim().isEmpty()) {
            AlertWindow.showError("Email not found", "Please enter a valid email address");
            return;
        }
        boolean confirmed = AlertWindow.showConfirmation(
                "Delete Confirmation",
                "Are you sure you want to delete user: " + email + "?"
        );
        if (confirmed) {
            AdminService adminService = new AdminService();
            adminService.deleteUser(email);
            NavigationManager.closeCurrentStage(deleteButton);
            AlertWindow.showInformation("Success", "User deleted successfully!");
        }
    }
}
