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
@Table(name = "order_info_table")
public class OrderInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderID")
    private Orders orders;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dishId")
    private Dish dish;
    private int quantity;
    private double price;
}
