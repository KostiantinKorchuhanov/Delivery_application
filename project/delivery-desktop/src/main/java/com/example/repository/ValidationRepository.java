package com.example.repository;

import com.example.entity.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

/**
 * Repository for performing data validation and uniqueness checks.
 *
 * @author Kostiantyn Korchuhanov
 * @version 1.0
 */
public class ValidationRepository {
    private final EntityManager entityManager;

    /**
     * @param entityManager JPA entity manager for database operations
     */
    public ValidationRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Checks if a specific value is unique across a given field in the User table. Used for registration and login operations.
     *
     * @param field the entity field name to check ("email", "username")
     * @param value the value to search for
     * @return true if the value does not exist, false otherwise
     */
    public boolean validateUnique(String field, String value) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<User> root = query.from(User.class);
        query.select(cb.count(root)).where(cb.equal(root.get(field), value));
        return entityManager.createQuery(query).getSingleResult() == 0;
    }
}