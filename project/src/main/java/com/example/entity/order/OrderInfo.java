package com.example.entity.order;

import com.example.entity.restaurant.Dish;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class OrderInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "orderID")
    private Orders orders;

    @ManyToOne
    @JoinColumn(name = "dishId")
    private Dish dish;

    private int quantity;
    private int price;

}
