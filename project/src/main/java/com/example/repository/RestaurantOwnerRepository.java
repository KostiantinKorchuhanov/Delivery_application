package com.example.repository;

import com.example.entity.restaurant.Restaurant;
import com.example.entity.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class RestaurantOwnerRepository {
    private final EntityManager entityManager;
    public RestaurantOwnerRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    public List<Restaurant> getRestaurantList(int ownerId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Restaurant> criteriaQuery = criteriaBuilder.createQuery(Restaurant.class);
        Root<Restaurant> root = criteriaQuery.from(Restaurant.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("owner").get("userId"), ownerId));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    public void addRestaurant(Restaurant restaurant) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(restaurant);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException(e);
        }
    }

    public void updateRestaurant(Restaurant restaurant) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(restaurant);
            entityManager.getTransaction().commit();
        }
        catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    public void deleteRestaurant(int restaurantId) {
        try {
            entityManager.getTransaction().begin();
            Restaurant restaurant = entityManager.find(Restaurant.class, restaurantId);
            if (restaurant != null){
                entityManager.remove(restaurant);
            }
            entityManager.getTransaction().commit();
        }
        catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }
}
