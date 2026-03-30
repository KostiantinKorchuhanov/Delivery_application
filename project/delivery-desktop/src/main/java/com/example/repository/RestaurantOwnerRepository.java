package com.example.repository;

import com.example.entity.restaurant.Restaurant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class RestaurantOwnerRepository {
    private final EntityManager entityManager;

    public RestaurantOwnerRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Restaurant> getRestaurantList(int ownerId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Restaurant> cq = cb.createQuery(Restaurant.class);
        Root<Restaurant> root = cq.from(Restaurant.class);
        cq.select(root).where(cb.equal(root.get("owner").get("userId"), ownerId));
        return entityManager.createQuery(cq).getResultList();
    }

    public void addRestaurant(Restaurant restaurant) {
        entityManager.persist(restaurant);
    }

    public void updateRestaurant(Restaurant restaurant) {
        entityManager.merge(restaurant);
    }

    public void deleteRestaurant(int restaurantId) {
        Restaurant restaurant = entityManager.find(Restaurant.class, restaurantId);
        if (restaurant != null) {
            entityManager.remove(restaurant);
        }
    }

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

