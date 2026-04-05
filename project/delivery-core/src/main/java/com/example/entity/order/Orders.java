package com.example.entity.order;


import com.example.entity.restaurant.Restaurant;
import com.example.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer's order within the system.
 * This entity manages the relationships between the customer, restaurant, and driver,
 * while also tracking the list of items and the total financial calculation.
 *
 * @author Kostiantyn Korchuhanov
 * @version 1.0
 * @see com.example.entity.user.User
 * @see com.example.entity.restaurant.Restaurant
 * @see OrderInfo
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders_table")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId")
    private User customer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurantId")
    private Restaurant restaurant;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driverId")
    private User driver;

    /**
     * The list of specific items (dishes and quantities) in this order.
     * Mapped by the "orders" field in {@link OrderInfo}.
     */
    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderInfo> orderItemList = new ArrayList<>();
    private double orderTotalPrice;
    private String status;

    /**
     * Adds an item to the order and automatically updates the
     * relationship and the total price.
     *
     * @param item The {@link OrderInfo} object to be added to the order.
     */
    public void addOrderItem(OrderInfo item) {
        orderItemList.add(item);
        item.setOrders(this);
        calculateTotalPrice();
    }

    /**
     * Recalculates the {@code orderTotalPrice} by summing the (price * quantity)
     * of all items in the order list.
     */
    public void calculateTotalPrice() {
        this.orderTotalPrice = orderItemList.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}