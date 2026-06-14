package com.example.entity.user;


import com.example.entity.order.Orders;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "driver_table")

public class Driver extends User {
    @OneToOne
    @JoinColumn(name = "currentOrderId")
    private Orders currentOrder;

    private double averageRating = 0.0;
    private int totalRatings = 0;

    public void addRating(int newRating) {
        double totalPoints = (averageRating * totalRatings) + newRating;
        totalRatings++;
        averageRating = totalPoints / totalRatings;
    }
}
