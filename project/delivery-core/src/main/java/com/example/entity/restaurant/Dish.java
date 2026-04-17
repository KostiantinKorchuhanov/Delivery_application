package com.example.entity.restaurant;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

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
    private double price;
    private boolean available = true;
    private Double specialPrice;
    private LocalTime specialPriceStart;
    private LocalTime specialPriceEnd;

    @Column(length = 1024)
    private String description;

    @ManyToOne
    @JoinColumn(name = "restaurantId")
    private Restaurant restaurant;

    public double getActivePrice() {
        LocalTime now = LocalTime.now();

        if (specialPrice != null && specialPriceStart != null && specialPriceEnd != null) {
            if (now.isAfter(specialPriceStart) && now.isBefore(specialPriceEnd)) {
                return specialPrice;
            }
        }
        return this.price;
    }
}

