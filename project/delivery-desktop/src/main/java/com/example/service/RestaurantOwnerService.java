package com.example.service;

import com.example.config.HibernateConfig;
import com.example.entity.restaurant.Restaurant;
import com.example.entity.user.RestaurantOwner;
import com.example.repository.RestaurantOwnerRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Service class for managing restaurant ownership logic.
 * Handles operations such as creation, updates, deletion, and searching of restaurants.
 *
 * @author Kostiantyn Korchuhanov
 * @version 1.0
 * <p>
 * Note: This documentation was written by an AI because the human developer is currently in a fetal position under the desk(please do not lover his grade)
 */
public class RestaurantOwnerService {

    /**
     * Finds all restaurants associated with a specific owner ID.
     *
     * @param ownerId unique identifier of the owner
     * @return list of owned restaurants
     */
    public List<Restaurant> findAllRestaurants(int ownerId) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            RestaurantOwnerRepository repo = new RestaurantOwnerRepository(em);
            return repo.getRestaurantList(ownerId);
        } finally {
            em.close();
        }
    }

    /**
     * Creates and persists a new restaurant for a given owner.
     *
     * @param name        name of the restaurant
     * @param description text description of the restaurant
     * @param address     physical address
     * @param ownerId     ID of the owning {@link RestaurantOwner}
     * @throws RuntimeException if the transaction fails
     */
    public void saveNewRestaurant(String name, String description, String address, int ownerId) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            em.getTransaction().begin();

            RestaurantOwner owner = em.find(RestaurantOwner.class, ownerId);
            if (owner != null) {
                Restaurant restaurant = new Restaurant();
                restaurant.setRestaurantName(name);
                restaurant.setDescription(description);
                restaurant.setAddress(address);
                restaurant.setOwner(owner);

                RestaurantOwnerRepository repo = new RestaurantOwnerRepository(em);
                repo.addRestaurant(restaurant);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Failed to save restaurant", e);
        } finally {
            em.close();
        }
    }

    /**
     * Updates an existing restaurant's profile.
     *
     * @param restaurant restaurant entity to merge
     */
    public void updateRestaurant(Restaurant restaurant) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            em.getTransaction().begin();

            RestaurantOwnerRepository repo = new RestaurantOwnerRepository(em);
            repo.updateRestaurant(restaurant);

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    /**
     * Removes a restaurant from the system by ID.
     *
     * @param restaurantId unique identifier of the restaurant to delete
     */
    public void deleteRestaurant(int restaurantId) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            em.getTransaction().begin();

            RestaurantOwnerRepository repo = new RestaurantOwnerRepository(em);
            repo.deleteRestaurant(restaurantId);

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    /**
     * Searches for restaurants belonging to an owner based on a search string.
     * Defaults to all restaurants if the search text is empty.
     *
     * @param ownerId    unique identifier of the owner
     * @param searchText query string to match against restaurant fields
     * @return list of matching restaurants
     */
    public List<Restaurant> searchRestaurants(int ownerId, String searchText) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            if (searchText == null || searchText.trim().isEmpty()) {
                return findAllRestaurants(ownerId);
            }
            RestaurantOwnerRepository repo = new RestaurantOwnerRepository(em);
            return repo.searchRestaurants(ownerId, searchText);
        } finally {
            em.close();
        }
    }
}