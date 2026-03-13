package com.example.ui.controller;

import com.example.app.NavigationManager;
import com.example.service.UserService;
import com.example.validation.UserFieldValidation;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class AdminRegisterWindow {
    private UserFieldValidation userFieldValidation = new UserFieldValidation();
    @FXML private Button registerButton;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField passwordField;
    @FXML private TextField repeatePasswordField;
    @FXML private TextField usernameField;
    @FXML private TextField phoneField;
    @FXML private TextField surnameField;
    @FXML private ComboBox<String> userType;

    @FXML private void initialize(){
        phoneField.setTextFormatter(userFieldValidation.getPhoneFormatter(9));

        registerButton.disableProperty().bind(Bindings.createBooleanBinding(() ->
                        !userFieldValidation.canBeRegistered(
                                usernameField.getText(),
                                passwordField.getText(),
                                repeatePasswordField.getText(),
                                phoneField.getText(),
                                emailField.getText(),
                                nameField.getText(),
                                surnameField.getText(),
                                userType.getValue()
                        ),
                usernameField.textProperty(),
                passwordField.textProperty(),
                repeatePasswordField.textProperty(),
                phoneField.textProperty(),
                emailField.textProperty(),
                nameField.textProperty(),
                surnameField.textProperty(),
                userType.valueProperty()
        ));
    }

    @FXML private void registerNewUser(){
        UserService userService = new UserService();
        userService.registerUser(
                nameField.getText(),
                surnameField.getText(),
                usernameField.getText(),
                emailField.getText(),
                passwordField.getText(),
                repeatePasswordField.getText(),
                phoneField.getText(),
                userType.getValue()
        );
        NavigationManager.closeCurrentStage(registerButton);
    }
}
