package com.example.entity.order;

import com.example.entity.restaurant.Dish;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a single line item within a specific order.
 * This entity links a {@link Dish} to an {@link Orders} object,
 * capturing the quantity and the price at the time of purchase.
 *
 * @author Kostiantyn Korchuhanov
 * @version 1.0
 * @see Orders
 * @see com.example.entity.restaurant.Dish
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_info_table")
public class OrderInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderID")
    private Orders orders;

    /**
     * The specific dish being ordered.
     * Linked via the "dishId" foreign key.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dishId")
    private Dish dish;
    private int quantity;
    private double price;
}
