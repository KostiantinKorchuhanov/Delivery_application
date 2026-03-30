package com.example.service;

import com.example.config.HibernateConfig;
import com.example.entity.order.Orders;
import com.example.repository.RestaurantOrderReporitory;
import jakarta.persistence.EntityManager;

import java.util.List;

public class RestaurantOrderService {
    public List<Orders> getAllOrdersForOwner(int ownerId) {
        try (EntityManager em = HibernateConfig.getEntityManager()) {
            RestaurantOrderReporitory repository = new RestaurantOrderReporitory(em);
            return repository.getOrdersByOwner(ownerId);
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
}

