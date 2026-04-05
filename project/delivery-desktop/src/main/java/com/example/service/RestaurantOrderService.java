package com.example.service;

import com.example.config.HibernateConfig;
import com.example.entity.order.Orders;
import com.example.entity.restaurant.Restaurant;
import com.example.repository.RestaurantOrderReporitory;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Service class for managing restaurant order operations.
 * Coordinates between the repository layer and transaction management.
 *
 * @author Kostiantyn Korchuhanov
 * @version 1.0
 * Note: If you're reading this, you’re either very bored or looking for a way to fix my mess. Good luck!
 */
public class RestaurantOrderService {

    /**
     * Retrieves all orders belonging to a specific restaurant owner.
     *
     * @param ownerId unique identifier of the owner
     * @return list of orders for the owner
     */
    public List<Orders> getAllOrdersForOwner(int ownerId) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            RestaurantOrderReporitory repository = new RestaurantOrderReporitory(em);
            return repository.getOrdersByOwner(ownerId);
        } finally {
            em.close();
        }
    }

    /**
     * Updates the status of an order within a transaction.
     *
     * @param orderId   ID of the order to update
     * @param newStatus the new status string to apply
     */
    public void updateStatus(int orderId, String newStatus) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            em.getTransaction().begin();
            RestaurantOrderReporitory repository = new RestaurantOrderReporitory(em);
            Orders order = em.find(Orders.class, orderId);
            if (order != null) {
                order.setStatus(newStatus);
                repository.update(order);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Retrieves all orders in the system with full details.
     *
     * @return list of all orders
     */
    public List<Orders> getAllOrders() {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            return new RestaurantOrderReporitory(em).findAllOrdersWithDetails();
        } finally {
            em.close();
        }
    }

    /**
     * Recalculates total price and updates an existing order.
     *
     * @param order order entity to update
     */
    public void updateOrder(Orders order) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            em.getTransaction().begin();
            order.calculateTotalPrice();
            new RestaurantOrderReporitory(em).updateOrder(order);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    /**
     * Persists a new order and calculates its initial total price.
     *
     * @param order new order entity to save
     */
    public void addOrder(Orders order) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            em.getTransaction().begin();
            order.calculateTotalPrice();
            new RestaurantOrderReporitory(em).addOrder(order);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    /**
     * Retrieves a list of all available restaurants.
     *
     * @return list of restaurant entities
     */
    public List<Restaurant> getAllRestaurants() {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            var cb = em.getCriteriaBuilder();
            var query = cb.createQuery(Restaurant.class);
            var root = query.from(Restaurant.class);
            query.select(root);
            return em.createQuery(query).getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Deletes a specific order from the system.
     *
     * @param order order entity to remove
     */
    public void deleteOrder(Orders order) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            em.getTransaction().begin();

            RestaurantOrderReporitory repo = new RestaurantOrderReporitory(em);
            repo.deleteOrder(order);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}