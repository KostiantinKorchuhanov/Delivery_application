package com.example.entity.user;


import com.example.entity.order.Orders;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Driver extends User {
    @OneToOne
    @JoinColumn(name = "currentOrderId")
    private Orders currentOrder;
}