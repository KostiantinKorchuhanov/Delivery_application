package com.example.entity.restaurant;

import com.example.entity.restaurant.Restaurant;
import com.example.entity.user.RestaurantOwner;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor

public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int dishId;
    private String dishName;
    private String description;
    private double price;
    private boolean available = true;

    @ManyToOne
    @JoinColumn(name = "restaurantId")
    private Restaurant restaurant;
}

