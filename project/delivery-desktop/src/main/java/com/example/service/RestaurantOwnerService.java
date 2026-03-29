package com.example.service;

import com.example.config.HibernateConfig;
import com.example.entity.restaurant.Dish;
import com.example.entity.restaurant.Restaurant;
import com.example.entity.user.RestaurantOwner;
import com.example.repository.RestaurantOwnerRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

public class RestaurantOwnerService {

    public List<Restaurant> findAllRestaurants(int ownerId) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            RestaurantOwnerRepository repo = new RestaurantOwnerRepository(em);
            return repo.getRestaurantList(ownerId);
        } finally {
            em.close();
        }
    }

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