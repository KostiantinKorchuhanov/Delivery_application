package com.example.validation.subvalidation;


import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class PasswordValidation {
    private static final Argon2 argon2 = Argon2Factory.create();

    public static String hashPassword(String password) {
        return argon2.hash(5, 65536, 2, password);
    }

    public static boolean validatePassword(String hash, String password) {
        return argon2.verify(hash, password);
    }
}

