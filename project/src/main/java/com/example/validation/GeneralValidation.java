package com.example.validation;

import com.example.validation.subvalidation.EmailValidation;
import com.example.validation.subvalidation.PhoneValidation;

public class GeneralValidation {
    public boolean validateEmail(String email) {
        return EmailValidation.validateEmail(email);
    }

    public boolean validatePhone(String phone) {
        return PhoneValidation.validatePhone(phone);
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
