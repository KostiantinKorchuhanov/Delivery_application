package com.example.model.user;


import jakarta.persistence.Entity;
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
    private String currentOrder;

    public void takeOrder(String orderId){
        this.currentOrder = orderId;
    }

    public void completeOrder(){
        this.currentOrder = null;
    }
}