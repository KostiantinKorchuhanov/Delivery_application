package com.example.repository;

import com.example.entity.order.OrderInfo;
import com.example.entity.order.Orders;
import com.example.entity.restaurant.Restaurant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;

import java.util.List;

/**
 * Repository for managing restaurant orders with detailed criteria fetching.
 *
 * @author Kostiantyn Korchuhanov
 * @version 1.0
 * Note: This class was once clean and beautiful. Then, reality happened. Please be kind to it.
 */
public class RestaurantOrderReporitory {
    private final EntityManager em;

    /**
     * @param em JPA entity manager for database operations
     */
    public RestaurantOrderReporitory(EntityManager em) {
        this.em = em;
    }

    /**
     * Retrieves all orders for a specific restaurant owner with pre-fetched details.
     *
     * @param ownerId unique identifier of the restaurant owner
     * @return list of detailed orders
     */
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

    /**
     * Merges the state of the given order into the current persistence context.
     *
     * @param order order entity to update
     */
    public void update(Orders order) {
        em.merge(order);
    }

    /**
     * Fetches all orders in the system with full relationship initialization.
     *
     * @return list of all orders with customer, restaurant, driver, and item details
     */
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

    /**
     * Persists a new order entity.
     *
     * @param order order to be saved
     */
    public void addOrder(Orders order) {
        em.persist(order);
    }

    /**
     * Updates an existing order entity.
     *
     * @param order order to be updated
     */
    public void updateOrder(Orders order) {
        em.merge(order);
    }

    /**
     * Removes an order from the database.
     *
     * @param order order entity to be deleted
     */
    public void deleteOrder(Orders order) {
        Orders managedOrder = em.merge(order);
        em.remove(managedOrder);
    }
}