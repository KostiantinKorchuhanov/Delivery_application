package com.example.repository;

import com.example.entity.restaurant.Dish;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class RestaurantRepository {
    private final EntityManager entityManager;

    public RestaurantRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Dish> getDishList(int restaurantId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Dish> cq = cb.createQuery(Dish.class);
        Root<Dish> root = cq.from(Dish.class);

        cq.select(root).where(cb.equal(root.get("restaurant").get("restaurantId"), restaurantId));

        return entityManager.createQuery(cq).getResultList();
    }

    public void addDish(Dish dish) {
        entityManager.persist(dish);
    }

    public void updateDish(Dish dish) {
        entityManager.merge(dish);
    }

    public void deleteDish(long dishId) {
        Dish dish = entityManager.find(Dish.class, dishId);
        if (dish != null) {
            entityManager.remove(dish);
        }
    }

    public List<Dish> searchDishes(int restaurantId, String searchText) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Dish> cq = cb.createQuery(Dish.class);
        Root<Dish> root = cq.from(Dish.class);
        Predicate restaurantPredicate = cb.equal(root.get("restaurant").get("restaurantId"), restaurantId);
        Predicate searchPredicate = cb.like(cb.lower(root.get("dishName")), "%" + searchText.toLowerCase() + "%");

        cq.select(root).where(cb.and(restaurantPredicate, searchPredicate));

        return entityManager.createQuery(cq).getResultList();
    }
}
