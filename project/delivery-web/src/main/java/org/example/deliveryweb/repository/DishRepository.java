package org.example.deliveryweb.repository;

import com.example.entity.restaurant.Dish;
import com.example.entity.restaurant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Integer> {
    List<Dish> findDishByRestaurant(Restaurant restaurant);

    List<Dish> findByRestaurantAndDishNameContainingIgnoreCase(Restaurant restaurant, String dishName);
}
