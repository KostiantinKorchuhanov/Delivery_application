package com.example.repository;

import jakarta.persistence.EntityManager;

public class RegisterRepository {
    private final EntityManager entityManager;

    public RegisterRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public <T> void save(T entity) {
        entityManager.persist(entity);
    }
}
