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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    @ManyToOne
    @JoinColumn(name = "customerId")
    private User customer;
    @ManyToOne
    @JoinColumn(name = "restaurantId")
    private Restaurant restaurant;
    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    private List<OrderInfo> orderItemList = new ArrayList<>();

    private double orderTotalPrice;
    private String status;
}
