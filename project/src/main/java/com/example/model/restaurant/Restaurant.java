package com.example.model.restaurant;

import com.example.model.user.RestaurantOwner;
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

public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int restaurantId;
    private String restaurantName;
    private String type;
    private String address;
    private String openingHours;
    @ManyToOne
    @JoinColumn(name = "ownerId")
    private RestaurantOwner owner;
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<Dish> menu;

    public Restaurant(int restaurantId, String restaurantName, String type, String address, String openingHours, RestaurantOwner owner) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.type = type;
        this.address = address;
        this.openingHours = openingHours;
        this.owner = owner;
        this.menu = new ArrayList<>();
    }

    public int getRestaurantId() { return restaurantId; }
    public String getRestaurantName() { return restaurantName; }
    public String getType() { return type; }
    public String getAddress() { return address; }
    public String getOpeningHours() { return openingHours; }
    public RestaurantOwner getOwner() { return owner; }
    public List<Dish> getMenu() { return menu; }

    public void addDish(Dish dish) {
        menu.add(dish);
    }
}
