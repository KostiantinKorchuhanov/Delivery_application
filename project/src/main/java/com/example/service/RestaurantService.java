package com.example.service;

import com.example.config.HibernateConfig;
import com.example.entity.restaurant.Dish;
import com.example.entity.restaurant.Restaurant;
import com.example.repository.RestaurantRepository;
import jakarta.persistence.EntityManager;
import java.util.List;

public class RestaurantService {

    public List<Dish> findAllDishes(int restaurantId) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            RestaurantRepository repo = new RestaurantRepository(em);
            return repo.getDishList(restaurantId);
        } finally {
            em.close();
        }
    }

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

    public void deleteDish(Dish dish) {
        if (dish == null) return;

        EntityManager em = HibernateConfig.getEntityManager();
        try {
            em.getTransaction().begin();

            RestaurantRepository repo = new RestaurantRepository(em);
            repo.deleteDish((long) dish.getDishId());

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

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