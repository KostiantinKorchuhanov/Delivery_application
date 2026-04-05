package com.example.repository;

import com.example.entity.restaurant.Dish;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.List;

/**
 * Repository for managing menu items (dishes) within a specific restaurant.
 * Handles database operations for menu items using JPA Criteria API.
 * * @author Kostiantyn Korchuhanov
 *
 * @version 1.0
 * Note: Reading this code causes mild existential dread and an intense urge to switch careers to sheep farming(they are so cute)
 */
public class RestaurantRepository {
    private final EntityManager entityManager;

    /**
     * @param entityManager JPA entity manager for database operations
     */
    public RestaurantRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Retrieves all dishes belonging to a specific restaurant.
     *
     * @param restaurantId unique identifier of the restaurant
     * @return list of dishes in the menu
     */
    public List<Dish> getDishList(int restaurantId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Dish> cq = cb.createQuery(Dish.class);
        Root<Dish> root = cq.from(Dish.class);

        cq.select(root).where(cb.equal(root.get("restaurant").get("restaurantId"), restaurantId));

        return entityManager.createQuery(cq).getResultList();
    }

    /**
     * Persists a new dish entity to the menu.
     *
     * @param dish dish to be saved
     */
    public void addDish(Dish dish) {
        entityManager.persist(dish);
    }

    /**
     * Updates an existing dish entity.
     *
     * @param dish dish containing updated information
     */
    public void updateDish(Dish dish) {
        entityManager.merge(dish);
    }

    /**
     * Deletes a dish from the database by its unique identifier.
     *
     * @param dishId ID of the dish to remove
     */
    public void deleteDish(long dishId) {
        Dish dish = entityManager.find(Dish.class, dishId);
        if (dish != null) {
            entityManager.remove(dish);
        }
    }

    /**
     * Searches for dishes within a restaurant matching a text pattern in the name.
     *
     * @param restaurantId unique identifier of the restaurant
     * @param searchText   search string for filtering dish names
     * @return list of matching dishes
     */
    public List<Dish> searchDishes(int restaurantId, String searchText) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Dish> cq = cb.createQuery(Dish.class);
        Root<Dish> root = cq.from(Dish.class);
        Predicate restaurantPredicate = cb.equal(root.get("restaurant").get("restaurantId"), restaurantId);
        Predicate searchPredicate = cb.like(cb.lower(root.get("dishName")), "%" + searchText.toLowerCase() + "%");

        cq.select(root).where(cb.and(restaurantPredicate, searchPredicate));

        return entityManager.createQuery(cq).getResultList();
    }
}