package com.example.entity.restaurant;

import com.example.entity.user.RestaurantOwner;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "restaurant_table")

public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int restaurantId;

    private String restaurantName;
    private String description;
    private String address;

    @ManyToOne
    @JoinColumn(name = "ownerId")
    private RestaurantOwner owner;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<RestaurantReview> reviews;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dish> menu;

    public Double getAverageRating() {
        if (reviews == null || reviews.isEmpty()) return 0.0;
        return reviews.stream()
                .mapToInt(RestaurantReview::getRating)
                .average()
                .orElse(0.0);
    }
}
