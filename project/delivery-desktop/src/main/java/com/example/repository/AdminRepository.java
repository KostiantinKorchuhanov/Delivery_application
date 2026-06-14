package com.example.repository;

import com.example.entity.order.Orders;
import com.example.entity.restaurant.Restaurant;
import com.example.entity.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class AdminRepository {
    private final EntityManager entityManager;

    public AdminRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public <T extends User> List<T> takeUsers(Class<T> userClass) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(userClass);
        Root<T> root = query.from(userClass);
        query.select(root);
        return entityManager.createQuery(query).getResultList();
    }

    public void updateUser(User user) {
        entityManager.merge(user);
    }

    public void deleteUser(User user) {
        User toRemove = entityManager.contains(user) ? user : entityManager.merge(user);
        entityManager.remove(toRemove);
    }

    public User findUserByEmail(String email) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root).where(builder.equal(root.get("email"), email));

        return entityManager.createQuery(query).getResultStream().findFirst().orElse(null);
    }

    public List<User> searchUsers(String searchText) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        String readyToSearch = "%" + searchText + "%";

        query.select(root).where(builder.or(
                builder.like(root.get("name"), readyToSearch),
                builder.like(root.get("email"), readyToSearch),
                builder.like(root.get("surname"), readyToSearch),
                builder.like(root.get("username"), readyToSearch)
        ));
        return entityManager.createQuery(query).getResultList();
    }

    public List<Restaurant> getAllRestaurants() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Restaurant> query = builder.createQuery(Restaurant.class);
        Root<Restaurant> root = query.from(Restaurant.class);
        query.select(root);
        return entityManager.createQuery(query).getResultList();
    }

    public Long countOrdersByStatus(String status) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Orders> root = query.from(Orders.class);
        query.select(cb.count(root)).where(cb.equal(root.get("status"), status));
        return entityManager.createQuery(query).getSingleResult();
    }

    public Double getTotalRevenue() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Double> query = cb.createQuery(Double.class);
        Root<Orders> root = query.from(Orders.class);
        query.select(cb.sum(root.get("orderTotalPrice")));
        return entityManager.createQuery(query).getSingleResult();
    }
}