package com.example.entity.restaurant;

import com.example.entity.user.RestaurantOwner;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Represents a Restaurant entity within the system.
 * This class stores restaurant details and maintains relationships with its
 * owner and its menu of dishes.
 *
 * @author Kostiantyn Korchuhanov
 * @version 1.0
 * @see com.example.entity.user.RestaurantOwner
 * @see Dish
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "restaurant_table")

public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int restaurantId;
    private String restaurantName;
    private String description;
    private String address;
    @ManyToOne
    @JoinColumn(name = "ownerId")
    private RestaurantOwner owner;

    /**
     * The list of dishes available at this restaurant.
     * Mapped by the "restaurant" field in the {@link Dish} entity.
     */
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dish> menu;
}
