package com.example.entity.order;


import com.example.entity.restaurant.Restaurant;
import com.example.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customerId")
    private User customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurantId")
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driverId")
    private User driver;

    private String review;
    private int driverRating;
    private int bonusesUsed;
    private double orderTotalPrice;
    private String status;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderInfo> orderItemList = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public void addOrderItem(OrderInfo item) {
        orderItemList.add(item);
        item.setOrders(this);
        calculateTotalPrice();
    }

    public void calculateTotalPrice() {
        this.orderTotalPrice = orderItemList.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}