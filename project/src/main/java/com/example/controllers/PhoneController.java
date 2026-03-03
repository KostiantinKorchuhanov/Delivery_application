package com.example.controllers;

import java.util.regex.Pattern;

public class PhoneController {
    public static boolean validatePhone(String phone) {
        String phoneRegex = "^[0-9]{9}$";
        Pattern pat = Pattern.compile(phoneRegex);
        return pat.matcher(phone).matches();
    }
}
