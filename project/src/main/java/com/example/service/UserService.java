package com.example.service;

import com.example.config.HibernateConfig;
import com.example.repository.LoginRepository;
import com.example.repository.RegisterRepository;
import com.example.entity.user.*;
import com.example.repository.ValidationRepository;
import com.example.validation.subvalidation.PasswordValidation;
import jakarta.persistence.EntityManager;

public class UserService {
    private RegisterRepository createOperation = new RegisterRepository();
    public void registerUser(String name, String surname, String username, String email, String password, String confirmPassword, String phoneNumber, String userType) {
        if (!password.equals(confirmPassword)) {
            System.out.println("Oh noooooo, passwords doo not match");
            return;
        }
        EntityManager entityManager = HibernateConfig.getEntityManager();
        try {
            ValidationRepository validationRepository = new ValidationRepository(entityManager);
            if (!validationRepository.validateUnique("email", email)) {
                System.out.println("Oh noooooo, email already exists");
                return;
            }
            if (!validationRepository.validateUnique("username", surname)) {
                System.out.println("Oh noooooo, username already exists");
                return;
            }
            User user;
            switch (userType) {
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
            user.setPasswordHash(PasswordValidation.hashPassword(password));
            createOperation.save(user);
        }
        finally {
            if  (entityManager != null &&  entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    public User loginUser(String email, String password) {
        EntityManager entityManager = com.example.config.HibernateConfig.getEntityManager();
        try {
            LoginRepository loginRepository = new LoginRepository(entityManager);
            User user = loginRepository.findUserByEmail(email);
            if (user == null) {
                System.out.println("Very very sad..... your user is not found");
                return null;
            }
            boolean validPassword = PasswordValidation.validatePassword(user.getPasswordHash(), password);
            if (validPassword) {
                System.out.println("UOhohoh user is valid))))))");
                return user;
            } else {
                System.out.println("Ooooooooo, password is invalid....");
                return null;
            }
        } finally {
            if (entityManager != null &&  entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }
}
