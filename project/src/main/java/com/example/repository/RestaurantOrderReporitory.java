package com.example.repository;

import com.example.config.HibernateConfig;
import com.example.entity.order.Orders;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;

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
        root.fetch("restaurant", JoinType.LEFT);

        cq.select(root).where(cb.equal(root.get("restaurant").get("owner").get("userId"), ownerId));

        return em.createQuery(cq).getResultList();
    }

    public void update(Orders order) {
        em.merge(order);
    }
}
