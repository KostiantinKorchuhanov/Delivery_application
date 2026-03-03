package com.example.service;

import com.example.controllers.PasswordController;
import com.example.model.operations.PerformLogin;
import com.example.model.operations.PerformRegistration;
import com.example.model.user.*;
import com.example.model.utils.HybernateUtils;
import jakarta.persistence.EntityManager;

public class UserService {
    private PerformRegistration createOperation = new PerformRegistration();
    public void registerUser(String name, String surname, String username, String email, String password, String confirmPassword, String phoneNumber, String userType) {
        if (!password.equals(confirmPassword)) {
            System.out.println("Oh noooooo, passwords doo not match");
            return;
        }
        User user;
        switch (userType){
            case "Customer":
                user = new Customer();
                break;
            case "Driver":
                user = new Driver();
                break;
            case "Restaurant":
                user = new RestaurantOwner();
                break;
            case "Admin":
                user = new Admin();
                break;
            default:
                return;
        }
        user.setName(name);
        user.setSurname(surname);
        user.setUsername(username);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setPasswordHash(PasswordController.hashPassword(password));
        createOperation.save(user);
    }

    public User loginUser(String email, String password) {
        EntityManager entityManager = HybernateUtils.getEntityManager();
        try {
            PerformLogin performLogin = new PerformLogin(entityManager);
            User user = performLogin.findUserByEmail(email);
            if (user == null) {
                System.out.println("Very very sad..... your user is not found");
                return null;
            }
            boolean validPassword = PasswordController.validatePassword(user.getPasswordHash(), password);
            if (validPassword) {
                System.out.println("UOhohoh user is valid))))))");
                return user;
            } else {
                System.out.println("Ooooooooo, password is invalid....");
                return null;
            }
        } finally {
            entityManager.close();
        }
    }
}
