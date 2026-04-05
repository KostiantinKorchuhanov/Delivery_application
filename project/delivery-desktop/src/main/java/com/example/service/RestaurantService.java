package com.example.service;

import com.example.config.HibernateConfig;
import com.example.entity.restaurant.Dish;
import com.example.entity.restaurant.Restaurant;
import com.example.repository.RestaurantRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Service class for managing restaurant menu items (dishes).
 * Coordinates business logic and transaction management for the restaurant menu.
 *
 * @author Kostiantyn Korchuhanov
 * @version 1.0
 * <p>
 * Note: Dear future me: I am so, so sorry. Please forgive me for what I have done here
 */
public class RestaurantService {

    /**
     * Retrieves all dishes belonging to a specific restaurant.
     *
     * @param restaurantId unique identifier of the restaurant
     * @return list of dishes in the menu
     */
    public List<Dish> findAllDishes(int restaurantId) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            RestaurantRepository repo = new RestaurantRepository(em);
            return repo.getDishList(restaurantId);
        } finally {
            em.close();
        }
    }

    /**
     * Associates a dish with a restaurant and persists it within a transaction.
     *
     * @param restaurantId ID of the restaurant to which the dish will be added
     * @param dish         dish entity to be saved
     */
    public void addDish(int restaurantId, Dish dish) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            em.getTransaction().begin();

            Restaurant managedRestaurant = em.find(Restaurant.class, restaurantId);
            if (managedRestaurant != null) {
                dish.setRestaurant(managedRestaurant);
                RestaurantRepository repo = new RestaurantRepository(em);
                repo.addDish(dish);
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
     * Updates an existing dish's information within a transaction.
     *
     * @param dish dish entity containing updated data
     */
    public void updateDish(Dish dish) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            em.getTransaction().begin();

            RestaurantRepository repo = new RestaurantRepository(em);
            repo.updateDish(dish);

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    /**
     * Deletes a dish from the menu using its ID within a transaction.
     *
     * @param dish dish entity to be removed
     */
    public void deleteDish(Dish dish) {
        if (dish == null) return;

        EntityManager em = HibernateConfig.getEntityManager();
        try {
            em.getTransaction().begin();

            RestaurantRepository repo = new RestaurantRepository(em);
            repo.deleteDish(dish.getDishId());

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    /**
     * Searches for dishes in a specific restaurant by a text pattern.
     *
     * @param restaurantId unique identifier of the restaurant
     * @param searchText   query string to match against dish names
     * @return list of matching dishes
     */
    public List<Dish> searchDishes(int restaurantId, String searchText) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            if (searchText == null || searchText.trim().isEmpty()) {
                return findAllDishes(restaurantId);
            }
            return new RestaurantRepository(em).searchDishes(restaurantId, searchText);
        } finally {
            em.close();
        }
    }
}
