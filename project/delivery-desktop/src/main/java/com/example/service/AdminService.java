package com.example.service;

import com.example.config.HibernateConfig;
import com.example.entity.order.Orders;
import com.example.entity.user.User;
import com.example.repository.AdminRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

public class AdminService {

    public <T extends User> List<T> allUsers(Class<T> userClass) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            return new AdminRepository(em).takeUsers(userClass);
        } finally {
            em.close();
        }
    }

    public void updateUser(User user) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            em.getTransaction().begin();
            new AdminRepository(em).updateUser(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void deleteUser(String email) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            em.getTransaction().begin();
            AdminRepository repo = new AdminRepository(em);
            User user = repo.findUserByEmail(email);
            if (user != null) {
                repo.deleteUser(user);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<User> searchUsers(String searchText) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            return new AdminRepository(em).searchUsers(searchText);
        } finally {
            em.close();
        }
    }

    public List<Orders> getAllOrders() {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            return new AdminRepository(em).findAllOrdersWithDetails();
        } finally {
            em.close();
        }
    }

    public void updateOrder(Orders order) {
        EntityManager em = HibernateConfig.getEntityManager();
        try {
            em.getTransaction().begin();
            order.calculateTotalPrice();
            new AdminRepository(em).updateOrder(order);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
