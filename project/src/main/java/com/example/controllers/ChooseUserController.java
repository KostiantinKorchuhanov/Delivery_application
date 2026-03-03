package com.example.controllers;


import com.example.app.Main;
import com.example.model.user.*;
import com.example.service.UserService;
import com.example.service.ValidationService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.stream.Stream;

public class ChooseUserController {
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
        Stream.of(nameField, surnameField, usernameField, emailFieldRegister, phoneField, passwordFieldRegister, repeatPasswordField).forEach(field -> field.textProperty().addListener((observable, oldValue, newValue) -> canBeRegistered()));
        selectUser.valueProperty().addListener((observable, oldValue, newValue) -> canBeRegistered());
        phoneField.setTextFormatter(new TextFormatter<String>(change -> {
            if  (change.getControlNewText().length() > 9) {
                return null;
            }
            return change;
        }));
        emailFieldLogin.textProperty().addListener((observable, oldValue, newValue) -> canBeLoggedIn());
        passwordFieldLogin.textProperty().addListener((observable, oldValue, newValue) -> canBeLoggedIn());
    }

    @FXML
    private void loginClicked() {
        loginClicked.setDisable(true);
        registerClicked.setDisable(false);
        registerBox.setVisible(false);
        loginBox.setVisible(true);
        AnimationController.fadeAnimation(loginBox);
        clearRegistrationFields();
        System.out.println("Login clocked");
    }

    @FXML
    private void registerClicked() {
        loginClicked.setDisable(false);
        registerClicked.setDisable(true);
        registerBox.setVisible(true);
        loginBox.setVisible(false);
        AnimationController.fadeAnimation(registerBox);
        clearLoginFields();
        System.out.println("register clicked");
    }

    @FXML
    private void registerNewUser(){
        String userType = userType();
        UserService userService = new UserService();
        userService.registerUser(nameField.getText(), surnameField.getText(), usernameField.getText(), emailFieldRegister.getText(), passwordFieldRegister.getText(), repeatPasswordField.getText(), phoneField.getText(), userType);
    }

    @FXML
    private void loginUser(){
        UserService userService = new UserService();
        User user = userService.loginUser(emailFieldLogin.getText(), passwordFieldLogin.getText());
        if (user != null){
            try {
                String whichFXML;
                if(user instanceof Admin){
                    whichFXML = "admin-main.fxml";
                }
                else if (user instanceof RestaurantOwner){
                    whichFXML = "restaurant-main.fxml";
                }
                else {
                    return;
                }
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(whichFXML));
                Stage stage = new Stage();
                stage.setScene(new Scene(fxmlLoader.load(), 800, 650));
                stage.setTitle("My cute admin");
                stage.setMinWidth(800);
                stage.setMinHeight(650);
                stage.show();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void canBeLoggedIn() {
        ValidationService validationService = new ValidationService();
        boolean canLogin = !emailFieldLogin.getText().isEmpty() && !passwordFieldLogin.getText().isEmpty() && validationService.validateEmail(emailFieldLogin.getText());
        loginButton.setDisable(!canLogin);
    }

    private void canBeRegistered() {
        ValidationService validationService = new ValidationService();
        boolean canRegister = validationService.validateEmail(emailFieldRegister.getText()) && validationService.validatePasswordMatch(passwordFieldRegister.getText(), repeatPasswordField.getText()) && validationService.validateName(nameField.getText()) && validationService.validateSurname(surnameField.getText()) && validationService.validateUsername(usernameField.getText()) && validationService.validatePhone(phoneField.getText()) && userType() != null;
        registerButton.setDisable(!canRegister);
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

    private String userType(){
        return selectUser.getValue();
    }
}


















