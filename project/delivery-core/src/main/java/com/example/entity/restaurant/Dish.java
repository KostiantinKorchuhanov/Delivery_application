package com.example.entity.restaurant;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a single menu item (Dish) within a restaurant.
 * This entity stores details about the dish's name, price, and availability status.
 *
 * @author Kostiantyn Korchuhanov
 * @version 1.0
 * @see Restaurant
 */
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "dish_table")

public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int dishId;
    private String dishName;
    @Column(length = 1024)
    private String description;
    private double price;
    private boolean available = true;

    /**
     * The restaurant to which this dish belongs.
     * This is a many-to-one relationship mapped to the "restaurantId" column.
     */
    @ManyToOne
    @JoinColumn(name = "restaurantId")
    private Restaurant restaurant;
}

