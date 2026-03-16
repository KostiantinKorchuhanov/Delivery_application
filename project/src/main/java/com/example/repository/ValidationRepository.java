package com.example.repository;

import com.example.config.HibernateConfig;
import com.example.entity.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class ValidationRepository {
    private EntityManager entityManager;
    public ValidationRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    public boolean validateUnique(String field, String email) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(criteriaBuilder.count(root)).where(criteriaBuilder.equal(root.get(field), email));
        return entityManager.createQuery(criteriaQuery).getSingleResult().longValue() == 0;
    }
}