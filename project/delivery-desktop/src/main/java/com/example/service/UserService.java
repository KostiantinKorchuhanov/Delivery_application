package com.example.service;

import com.example.config.HibernateConfig;
import com.example.entity.user.*;
import com.example.repository.LoginRepository;
import com.example.repository.RegisterRepository;
import com.example.repository.ValidationRepository;
import com.example.ui.helper.AlertWindow;
import com.example.validation.subvalidation.PasswordValidation;
import jakarta.persistence.EntityManager;

/**
 * Service class managing user lifecycle operations such as registration and authentication.
 * Handles input validation, password hashing, and user type instantiation.
 *
 * @author Kostiantyn Korchuhanov
 * @version 1.0
 * <p>
 * Note: I trust this code as much as I trust a "free Wi-Fi" sign in a dark alley(which basically was written by me)
 */
public class UserService {

    /**
     * Registers a new user with validation for unique identifiers and password matching.
     *
     * @param name            user's first name
     * @param surname         user's last name
     * @param username        chosen unique username
     * @param email           unique email address
     * @param password        plain text password
     * @param confirmPassword password confirmation for matching
     * @param phoneNumber     contact phone number
     * @param userType        string representing the role (Driver, Customer, etc.)
     * @return true if registration succeeded, false otherwise
     */
    public boolean registerUser(String name, String surname, String username, String email, String password, String confirmPassword, String phoneNumber, String userType) {
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

    /**
     * Authenticates a user by checking credentials against the database.
     *
     * @param email    email or username identifier
     * @param password plain text password to verify
     * @return the authenticated {@link User} object, or null if credentials fail
     */
    public User loginUser(String email, String password) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            LoginRepository loginRepo = new LoginRepository(em);
            User user = loginRepo.findByEmailOrUsername(email);
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

    /**
     * Factory method to create specific User implementations based on role.
     *
     * @param userType string identifier of the user role
     * @return a new instance of a {@link User} subclass, or null if type is invalid
     */
    private User createUserByType(String userType) {
        return switch (userType) {
            case "Driver" -> new Driver();
            case "Customer" -> new Customer();
            case "Restaurant" -> new RestaurantOwner();
            case "Admin" -> new Admin();
            default -> null;
        };
    }
}