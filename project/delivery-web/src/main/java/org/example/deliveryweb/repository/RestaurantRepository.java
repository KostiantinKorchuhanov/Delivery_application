package org.example.deliveryweb.repository;

import com.example.entity.restaurant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    List<Restaurant> findByRestaurantNameContainingIgnoreCase(String name);
}
