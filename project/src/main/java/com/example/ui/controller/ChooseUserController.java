package com.example.ui.controller;


import com.example.app.NavigationManager;
import com.example.entity.user.*;
import com.example.service.UserService;
import com.example.ui.util.FadeAnimation;
import com.example.validation.UserFieldValidation;
import javafx.beans.binding.Bindings;
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

    @FXML
    private void initialize() {
        phoneField.setTextFormatter(userFieldValidation.getPhoneFormatter(9));

        registerButton.disableProperty().bind(Bindings.createBooleanBinding(() ->
                        !userFieldValidation.canBeRegistered(
                                usernameField.getText(),
                                passwordFieldRegister.getText(),
                                repeatPasswordField.getText(),
                                phoneField.getText(),
                                emailFieldRegister.getText(),
                                nameField.getText(),
                                surnameField.getText(),
                                selectUser.getValue()
                        ),
                usernameField.textProperty(),
                passwordFieldRegister.textProperty(),
                repeatPasswordField.textProperty(),
                phoneField.textProperty(),
                emailFieldRegister.textProperty(),
                nameField.textProperty(),
                surnameField.textProperty(),
                selectUser.valueProperty()
        ));

        loginButton.disableProperty().bind(Bindings.createBooleanBinding(() ->
                        !userFieldValidation.canBeLoggedIn(emailFieldLogin.getText(), passwordFieldLogin.getText()),
                emailFieldLogin.textProperty(),
                passwordFieldLogin.textProperty()
        ));
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
        NavigationManager.closeCurrentStage(loginButton);
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

    // This is soooo stupid DO NOT FORGET TO DELETE THIS DISGUSTING THING
    public void dirtyOperation(){
        emailFieldLogin.setText("ff@ff.ff");
        passwordFieldLogin.setText("1111");
        loginButton.fire();
    }
}
