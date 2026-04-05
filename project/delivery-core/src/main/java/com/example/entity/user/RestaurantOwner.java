package com.example.entity.user;


import com.example.entity.restaurant.Restaurant;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Represents a specialized user role for Restaurant Owners.
 * Extends the base {@link User} class and maintains a collection of managed restaurants.
 *
 * @author Kostiantyn Korchuhanov
 * @version 1.0
 * @see User
 * @see com.example.entity.restaurant.Restaurant
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "restaurant_owner_table")

public class RestaurantOwner extends User {
    /**
     * A list of restaurants owned and managed by this user.
     * Mapped by the "owner" field in the {@link Restaurant} entity.
     */
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Restaurant> restaurantList;
}
