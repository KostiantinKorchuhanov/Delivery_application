package com.example.validation;

import com.example.validation.subvalidation.EmailValidation;
import javafx.scene.control.TextFormatter;

public class UserFieldValidation {
    private final GeneralValidation generalValidation = new GeneralValidation();

    public boolean canBeLoggedIn(String email, String password) {
        return !email.isEmpty() && !password.isEmpty() && EmailValidation.validateEmail(email);
    }

    public boolean canBeRegistered(String username, String password, String repeatedPassword, String phone, String email, String name, String surname, String userType) {
        return EmailValidation.validateEmail(email)
                && generalValidation.validateName(name)
                && generalValidation.validatePhone(phone)
                && generalValidation.validateSurname(surname)
                && generalValidation.validateUsername(username)
                && generalValidation.validatePasswordMatch(password, repeatedPassword)
                && userType != null;
    }


    public TextFormatter<String> getPhoneFormatter(int maxLength) {
        return new TextFormatter<>(change ->
        {
            if (change.getControlNewText().length() > maxLength) {
                return null;
            }
            return change;
        });
    }

    public boolean canBeDeleted(String email) {
        return generalValidation.validateEmail(email);
    }
}
