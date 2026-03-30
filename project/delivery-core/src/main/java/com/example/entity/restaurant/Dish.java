package com.example.entity.restaurant;

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

    @ManyToOne
    @JoinColumn(name = "restaurantId")
    private Restaurant restaurant;
}

