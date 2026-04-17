package com.example.repository;

import com.example.entity.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class LoginRepository {
    private final EntityManager entityManager;

    public LoginRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public User findByEmailOrUsername(String identifier) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root).where(
                cb.or(
                        cb.equal(root.get("email"), identifier),
                        cb.equal(root.get("username"), identifier)
                )
        );
        return entityManager.createQuery(query).getResultStream().findFirst().orElse(null);
    }
}