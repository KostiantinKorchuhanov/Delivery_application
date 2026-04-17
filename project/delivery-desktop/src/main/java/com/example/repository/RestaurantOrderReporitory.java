package com.example.repository;

import com.example.entity.order.OrderInfo;
import com.example.entity.order.Orders;
import com.example.entity.restaurant.Restaurant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;

import java.util.List;


public class RestaurantOrderReporitory {
    private final EntityManager em;

    public RestaurantOrderReporitory(EntityManager em) {
        this.em = em;
    }

    public List<Orders> getOrdersByOwner(int ownerId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Orders> cq = cb.createQuery(Orders.class);
        Root<Orders> root = cq.from(Orders.class);
        root.fetch("customer", JoinType.LEFT);
        Fetch<Orders, Restaurant> restaurantFetch = root.fetch("restaurant", JoinType.LEFT);
        Fetch<Orders, OrderInfo> itemFetch = root.fetch("orderItemList", JoinType.LEFT);
        itemFetch.fetch("dish", JoinType.LEFT);
        cq.select(root).distinct(true);
        cq.where(cb.equal(root.get("restaurant").get("owner").get("userId"), ownerId));
        cq.orderBy(cb.desc(root.get("orderId")));
        return em.createQuery(cq).getResultList();
    }

    public void update(Orders order) {
        em.merge(order);
    }

    public List<Orders> findAllOrdersWithDetails() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Orders> cq = cb.createQuery(Orders.class);
        Root<Orders> root = cq.from(Orders.class);
        root.fetch("customer", JoinType.LEFT);
        root.fetch("restaurant", JoinType.LEFT);
        root.fetch("driver", JoinType.LEFT);
        Fetch<Orders, OrderInfo> itemFetch = root.fetch("orderItemList", JoinType.LEFT);
        itemFetch.fetch("dish", JoinType.LEFT);

        cq.select(root).distinct(true);
        cq.orderBy(cb.desc(root.get("orderId")));

        return em.createQuery(cq).getResultList();
    }

    public void addOrder(Orders order) {
        em.persist(order);
    }

    public void updateOrder(Orders order) {
        em.merge(order);
    }

    public void deleteOrder(Orders order) {
        Orders managedOrder = em.merge(order);
        em.remove(managedOrder);
    }
}