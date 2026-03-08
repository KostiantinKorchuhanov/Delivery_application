package com.example.ui.controller;


import com.example.app.NavigationManager;
import com.example.entity.user.*;
import com.example.service.UserService;
import com.example.ui.util.FadeAnimation;
import com.example.validation.UserFieldValidation;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.stream.Stream;

public class ChooseUserController {
    private UserFieldValidation  userFieldValidation = new UserFieldValidation();
    @FXML private VBox registerBox;
    @FXML private VBox loginBox;
    @FXML private TextField nameField;
    @FXML private TextField surnameField;
    @FXML private TextField usernameField;
    @FXML private TextField emailFieldRegister;
    @FXML private TextField emailFieldLogin;
    @FXML private TextField phoneField;
    @FXML private PasswordField passwordFieldRegister;
    @FXML private PasswordField passwordFieldLogin;
    @FXML private PasswordField repeatPasswordField;
    @FXML private ToggleButton registerClicked;
    @FXML private ToggleButton loginClicked;
    @FXML private ComboBox<String> selectUser;
    @FXML private Button registerButton;
    @FXML private Button loginButton;

    @FXML private void initialize() {
        Stream.of(nameField, surnameField, usernameField, emailFieldRegister, phoneField, passwordFieldRegister, repeatPasswordField)
                .forEach(field -> field.textProperty().addListener((obs, oldVal, newVal) ->
                        registerButton.setDisable(!userFieldValidation.canBeRegistered(
                                emailFieldRegister.getText(),
                                passwordFieldRegister.getText(),
                                repeatPasswordField.getText(),
                                nameField.getText(),
                                surnameField.getText(),
                                usernameField.getText(),
                                phoneField.getText(),
                                selectUser.getValue()
                        ))
                ));
        selectUser.valueProperty().addListener((obs, oldVal, newVal) ->
                registerButton.setDisable(!userFieldValidation.canBeRegistered(
                        emailFieldRegister.getText(),
                        passwordFieldRegister.getText(),
                        repeatPasswordField.getText(),
                        nameField.getText(),
                        surnameField.getText(),
                        usernameField.getText(),
                        phoneField.getText(),
                        selectUser.getValue()
                ))
        );
        phoneField.setTextFormatter(userFieldValidation.getPhoneFormatter(9));
        emailFieldLogin.textProperty().addListener((obs, oldVal, newVal) ->
                loginButton.setDisable(!userFieldValidation.canBeLoggedIn(emailFieldLogin.getText(), passwordFieldLogin.getText()))
        );
        passwordFieldLogin.textProperty().addListener((obs, oldVal, newVal) ->
                loginButton.setDisable(!userFieldValidation.canBeLoggedIn(emailFieldLogin.getText(), passwordFieldLogin.getText()))
        );
    }

    @FXML
    private void loginClicked() {
        loginClicked.setDisable(true);
        registerClicked.setDisable(false);
        registerBox.setVisible(false);
        loginBox.setVisible(true);
        FadeAnimation.fadeAnimation(loginBox);
        clearRegistrationFields();
        System.out.println("Login clocked");
    }

    @FXML
    private void registerClicked() {
        loginClicked.setDisable(false);
        registerClicked.setDisable(true);
        registerBox.setVisible(true);
        loginBox.setVisible(false);
        FadeAnimation.fadeAnimation(registerBox);
        clearLoginFields();
        System.out.println("register clicked");
    }

    @FXML
    private void registerNewUser(){
        String userType = selectUser.getValue();
        UserService userService = new UserService();
        userService.registerUser(
                nameField.getText(),
                surnameField.getText(),
                usernameField.getText(),
                emailFieldRegister.getText(),
                passwordFieldRegister.getText(),
                repeatPasswordField.getText(),
                phoneField.getText(), userType);
    }

    @FXML
    private void loginUser() throws IOException {
        UserService userService = new UserService();
        User user = userService.loginUser(
                emailFieldLogin.getText(),
                passwordFieldLogin.getText());
        NavigationManager.navigateToUserList(user);
    }

    private void clearRegistrationFields(){
        nameField.clear();
        surnameField.clear();
        usernameField.clear();
        phoneField.clear();
        passwordFieldRegister.clear();
        repeatPasswordField.clear();
        emailFieldRegister.clear();
    }

    private void clearLoginFields(){
        emailFieldLogin.clear();
        passwordFieldLogin.clear();
    }
}
