package com.example.repository;

import com.example.entity.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;


public class ValidationRepository {
    private final EntityManager entityManager;

    public ValidationRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    public boolean validateUnique(String field, String value) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<User> root = query.from(User.class);
        query.select(cb.count(root)).where(cb.equal(root.get(field), value));
        return entityManager.createQuery(query).getSingleResult() == 0;
    }
}