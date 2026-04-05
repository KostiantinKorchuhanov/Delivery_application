package com.example.repository;

import com.example.entity.restaurant.Restaurant;
import com.example.entity.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

/**
 * Repository for administrative data operations using JPA Criteria API.
 *
 * @author Kostiantyn Korchuhanov
 * @version 1.0
 * Note: This repository is haunted. Please speak to the entities gently or they might vanishhhhhh
 */
public class AdminRepository {
    private final EntityManager entityManager;

    /**
     * @param entityManager JPA entity manager for DB operations
     */
    public AdminRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Retrieves all users of a specific subtype (Customer, Driver, etc.).
     *
     * @param userClass the class of the user type to fetch
     * @return list of matching users
     */
    public <T extends User> List<T> takeUsers(Class<T> userClass) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(userClass);
        Root<T> root = query.from(userClass);
        query.select(root);
        return entityManager.createQuery(query).getResultList();
    }

    /**
     * Updates an existing user's data in the database.
     *
     * @param user user entity to merge
     */
    public void updateUser(User user) {
        entityManager.merge(user);
    }

    /**
     * Removes a user from the database.
     *
     * @param user user entity to delete
     */
    public void deleteUser(User user) {
        User toRemove = entityManager.contains(user) ? user : entityManager.merge(user);
        entityManager.remove(toRemove);
    }

    /**
     * Searches for a user by their unique email.
     *
     * @param email search criteria
     * @return found User or null
     */
    public User findUserByEmail(String email) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root).where(builder.equal(root.get("email"), email));

        return entityManager.createQuery(query).getResultStream().findFirst().orElse(null);
    }

    /**
     * Searches users by name, surname, username, or email using partial matching.
     *
     * @param searchText text to match
     * @return list of matching users
     */
    public List<User> searchUsers(String searchText) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        String readyToSearch = "%" + searchText + "%";

        query.select(root).where(builder.or(
                builder.like(root.get("name"), readyToSearch),
                builder.like(root.get("email"), readyToSearch),
                builder.like(root.get("surname"), readyToSearch),
                builder.like(root.get("username"), readyToSearch)
        ));
        return entityManager.createQuery(query).getResultList();
    }

    /**
     * Fetches all restaurants from the system.
     *
     * @return list of all restaurants
     */
    public List<Restaurant> getAllRestaurants() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Restaurant> query = builder.createQuery(Restaurant.class);
        Root<Restaurant> root = query.from(Restaurant.class);
        query.select(root);
        return entityManager.createQuery(query).getResultList();
    }
}