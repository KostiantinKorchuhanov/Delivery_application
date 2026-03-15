package com.example.service;

import com.example.config.HibernateConfig;
import com.example.entity.restaurant.Restaurant;
import com.example.entity.user.RestaurantOwner;
import com.example.repository.RestaurantOwnerRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

public class RestaurantOwnerService {
    public List<Restaurant> findAllRestaurants(int ownerId) {
        EntityManager entityManager = HibernateConfig.getEntityManager();
        try {
            RestaurantOwnerRepository restaurantOwnerRepository = new RestaurantOwnerRepository(entityManager);
            return restaurantOwnerRepository.getRestaurantList(ownerId);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    public void saveNewRestaurant(String name, String description, String address, int ownerId) {
        EntityManager entityManager = HibernateConfig.getEntityManager();
        try {
            RestaurantOwnerRepository restaurantOwnerRepository = new RestaurantOwnerRepository(entityManager);
            RestaurantOwner restaurantOwner = entityManager.find(RestaurantOwner.class, ownerId);
            Restaurant restaurant = new Restaurant();
            restaurant.setRestaurantName(name);
            restaurant.setDescription(description);
            restaurant.setAddress(address);
            restaurant.setOwner(restaurantOwner);
            restaurantOwnerRepository.addRestaurant(restaurant);
        }
        finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }
}