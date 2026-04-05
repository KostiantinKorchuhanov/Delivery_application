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


/**
 * Represents a specialized user role for Drivers in the system.
 * Extends the base {@link User} class and handles the delivery of orders.
 *
 * @author Kostiantyn Korchuhanov
 * @version 1.0
 * @see User
 * @see com.example.entity.order.Orders
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "driver_table")

public class Driver extends User {
    /**
     * The specific order currently assigned to this driver for delivery.
     * This is a one-to-one relationship mapped via the "currentOrderId" column.
     */
    @OneToOne
    @JoinColumn(name = "currentOrderId")
    private Orders currentOrder;
}
