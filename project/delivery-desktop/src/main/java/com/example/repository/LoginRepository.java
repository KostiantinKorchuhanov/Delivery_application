package com.example.repository;

import com.example.config.HibernateConfig;
import com.example.entity.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class LoginRepository {
    private final EntityManager entityManager;
    public LoginRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public User findUserByEmail(String email) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root).where(builder.equal(root.get("email"), email));

        return entityManager.createQuery(query).getResultStream().findFirst().orElse(null);
    }
}
