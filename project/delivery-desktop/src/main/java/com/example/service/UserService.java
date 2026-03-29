package com.example.service;

import com.example.config.HibernateConfig;
import com.example.repository.LoginRepository;
import com.example.repository.RegisterRepository;
import com.example.entity.user.*;
import com.example.repository.ValidationRepository;
import com.example.ui.helper.AlertWindow;
import com.example.validation.subvalidation.PasswordValidation;
import jakarta.persistence.EntityManager;

public class UserService {

    public boolean registerUser(String name, String surname, String username, String email,
                                String password, String confirmPassword, String phoneNumber, String userType) {
        if (!password.equals(confirmPassword)) {
            AlertWindow.showError("Registration Error", "Passwords do not match!");
            return false;
        }
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            em.getTransaction().begin();
            ValidationRepository valRepo = new ValidationRepository(em);
            if (!valRepo.validateUnique("email", email)) {
                AlertWindow.showError("Registration Error", "Email already exists!");
                return false;
            }
            if (!valRepo.validateUnique("username", username)) {
                AlertWindow.showError("Registration Error", "Username already exists!");
                return false;
            }
            User user = createUserByType(userType);
            if (user == null) return false;
            user.setName(name);
            user.setSurname(surname);
            user.setUsername(username);
            user.setEmail(email);
            user.setPhoneNumber(phoneNumber);
            user.setPasswordHash(PasswordValidation.hashPassword(password));
            RegisterRepository regRepo = new RegisterRepository(em);
            regRepo.save(user);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            AlertWindow.showError("Database Error", "Something went wrong during registration.");
            return false;
        } finally {
            em.close();
        }
    }

    public User loginUser(String email, String password) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            LoginRepository loginRepo = new LoginRepository(em);
            User user = loginRepo.findUserByEmail(email);

            if (user != null && PasswordValidation.validatePassword(user.getPasswordHash(), password)) {
                System.out.println("Login successful");
                return user;
            }
            System.out.println("Invalid email or password");
            return null;
        } finally {
            em.close();
        }
    }

    private User createUserByType(String userType) {
        return switch (userType) {
            case "Restaurant" -> new RestaurantOwner();
            case "Admin" -> new Admin();
            default -> null;
        };
    }
}
