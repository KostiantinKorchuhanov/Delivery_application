package com.example.service;

import com.example.controllers.EmailController;
import com.example.controllers.PhoneController;

public class ValidationService {
    public boolean validateEmail(String email) {
        return EmailController.validateEmail(email);
    }

    public boolean validatePhone(String phone) {
        return PhoneController.validatePhone(phone);
    }

    public boolean validatePasswordMatch(String password, String repeatPassword) {
        return password != null && password.equals(repeatPassword);
    }

    public boolean validateName(String name) {
        return name != null && !name.isEmpty();
    }

    public boolean validateSurname(String surname) {
        return surname != null && !surname.isEmpty();
    }

    public boolean validateUsername(String username) {
        return username != null && !username.isEmpty();
    }
}
