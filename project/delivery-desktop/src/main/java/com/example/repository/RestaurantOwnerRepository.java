package com.example.repository;

import com.example.entity.restaurant.Restaurant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.List;

/**
 * Repository for managing restaurant-related data for specific owners.
 * Uses JPA Criteria API just because I decided so.
 *
 * @author Kostiantyn Korchuhanov
 * @version 1.0
 * <p>
 * Note: If you reached this point without a headache, you are welcome to the shizophrenia club (it would be funny if even this club only my imagination)
 */
public class RestaurantOwnerRepository {
    private final EntityManager entityManager;

    /**
     * @param entityManager JPA entity manager for database operations
     */
    public RestaurantOwnerRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Retrieves all restaurants belonging to a specific owner.
     *
     * @param ownerId unique identifier of the owner
     * @return list of restaurants
     */
    public List<Restaurant> getRestaurantList(int ownerId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Restaurant> cq = cb.createQuery(Restaurant.class);
        Root<Restaurant> root = cq.from(Restaurant.class);
        cq.select(root).where(cb.equal(root.get("owner").get("userId"), ownerId));
        return entityManager.createQuery(cq).getResultList();
    }

    /**
     * Saves a new restaurant entity to the database.
     *
     * @param restaurant restaurant to be persisted
     */
    public void addRestaurant(Restaurant restaurant) {
        entityManager.persist(restaurant);
    }

    /**
     * Updates an existing restaurant's information.
     *
     * @param restaurant restaurant entity to merge
     */
    public void updateRestaurant(Restaurant restaurant) {
        entityManager.merge(restaurant);
    }

    /**
     * Removes a restaurant from the database by its ID.
     *
     * @param restaurantId unique identifier of the restaurant
     */
    public void deleteRestaurant(int restaurantId) {
        Restaurant restaurant = entityManager.find(Restaurant.class, restaurantId);
        if (restaurant != null) {
            entityManager.remove(restaurant);
        }
    }

    /**
     * Searches for restaurants owned by a specific user based on a text pattern.
     * Matches against name, description, or address.
     *
     * @param ownerId    unique identifier of the owner
     * @param searchText query string for filtering
     * @return list of matching restaurants
     */
    public List<Restaurant> searchRestaurants(int ownerId, String searchText) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Restaurant> cq = cb.createQuery(Restaurant.class);
        Root<Restaurant> root = cq.from(Restaurant.class);
        String pattern = "%" + searchText.toLowerCase() + "%";
        Predicate ownerPredicate = cb.equal(root.get("owner").get("userId"), ownerId);

        Predicate searchPredicate = cb.or(
                cb.like(cb.lower(root.get("restaurantName")), pattern),
                cb.like(cb.lower(root.get("description")), pattern),
                cb.like(cb.lower(root.get("address")), pattern)
        );
        cq.select(root).where(cb.and(ownerPredicate, searchPredicate));
        return entityManager.createQuery(cq).getResultList();
    }
}