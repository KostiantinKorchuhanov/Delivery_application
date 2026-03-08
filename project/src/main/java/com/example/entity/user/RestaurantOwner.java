package com.example.entity.user;


import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class RestaurantOwner extends User{
    private String restaurantName;

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }
}