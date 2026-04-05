package com.example.service;

import com.example.config.HibernateConfig;
import com.example.entity.restaurant.Restaurant;
import com.example.entity.user.User;
import com.example.repository.AdminRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Service class for administrative operations, managing users and restaurants.
 * Handles transaction management and entity manager lifecycle.
 *
 * @author Kostiantyn Korchuhanov
 * @version 1.0
 * <p>
 * Note: This code works on dark energy and sheer willpower. Do not touch anything unless you are prepared for the consequences. Even admin with full CRUD wont help you
 */
public class AdminService {

    /**
     * Retrieves all users of a specific type.
     *
     * @param userClass the class of the user subtype to fetch
     * @param <T>       the type extending User
     * @return list of users
     */
    public <T extends User> List<T> allUsers(Class<T> userClass) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            return new AdminRepository(em).takeUsers(userClass);
        } finally {
            em.close();
        }
    }

    /**
     * Updates user information within a transaction.
     *
     * @param user user entity to update
     */
    public void updateUser(User user) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            em.getTransaction().begin();
            new AdminRepository(em).updateUser(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    /**
     * Deletes a user identified by email within a transaction.
     *
     * @param email unique email of the user to delete
     */
    public void deleteUser(String email) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            em.getTransaction().begin();
            AdminRepository repo = new AdminRepository(em);
            User user = repo.findUserByEmail(email);
            if (user != null) {
                repo.deleteUser(user);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    /**
     * Searches for users based on a partial text match.
     *
     * @param searchText query string
     * @return list of matching users
     */
    public List<User> searchUsers(String searchText) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            return new AdminRepository(em).searchUsers(searchText);
        } finally {
            em.close();
        }
    }

    /**
     * Retrieves all restaurants registered in the system.
     *
     * @return list of all restaurants
     */
    public List<Restaurant> getAllRestaurants() {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            return new AdminRepository(em).getAllRestaurants();
        } finally {
            em.close();
        }
    }
}
