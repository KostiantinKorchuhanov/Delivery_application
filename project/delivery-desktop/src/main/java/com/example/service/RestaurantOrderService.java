package com.example.service;

import com.example.config.HibernateConfig;
import com.example.entity.order.Orders;
import com.example.entity.restaurant.Restaurant;
import com.example.repository.RestaurantOrderReporitory;
import jakarta.persistence.EntityManager;

import java.util.List;


public class RestaurantOrderService {
    public List<Orders> getAllOrdersForOwner(int ownerId) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            RestaurantOrderReporitory repository = new RestaurantOrderReporitory(em);
            return repository.getOrdersByOwner(ownerId);
        } finally {
            em.close();
        }
    }

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

    public List<Orders> getAllOrders() {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            return new RestaurantOrderReporitory(em).findAllOrdersWithDetails();
        } finally {
            em.close();
        }
    }

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