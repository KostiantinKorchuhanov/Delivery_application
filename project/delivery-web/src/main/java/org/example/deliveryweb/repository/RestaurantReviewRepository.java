package org.example.deliveryweb.repository;

import com.example.entity.restaurant.Restaurant;
import com.example.entity.restaurant.RestaurantReview;
import com.example.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantReviewRepository extends JpaRepository<RestaurantReview, Integer> {
    boolean existsByUserAndRestaurant(User user, Restaurant restaurant);
}
