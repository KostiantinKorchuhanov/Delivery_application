package com.example.validation.subvalidation;

import java.util.regex.Pattern;

public class EmailValidation {
    public static boolean validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        return email != null && pat.matcher(email).matches();
    }
}

