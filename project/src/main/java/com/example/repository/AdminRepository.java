package com.example.repository;

import com.example.config.HibernateConfig;
import com.example.entity.order.OrderInfo;
import com.example.entity.order.Orders;
import com.example.entity.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;

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

    public List<Orders> findAllOrdersWithDetails() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Orders> cq = cb.createQuery(Orders.class);
        Root<Orders> root = cq.from(Orders.class);
        root.fetch("customer", JoinType.LEFT);
        root.fetch("restaurant", JoinType.LEFT);
        Fetch<Orders, OrderInfo> itemFetch = root.fetch("orderItemList", JoinType.LEFT);
        itemFetch.fetch("dish", JoinType.LEFT);

        cq.select(root).distinct(true);
        cq.orderBy(cb.desc(root.get("orderId")));

        return entityManager.createQuery(cq).getResultList();
    }

    public void updateOrder(Orders order) {
        entityManager.merge(order);
    }
}