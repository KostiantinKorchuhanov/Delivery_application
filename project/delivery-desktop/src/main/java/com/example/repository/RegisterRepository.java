package com.example.repository;

import jakarta.persistence.EntityManager;

/**
 * Repository for persisting new entities during registration.
 *
 * @author Kostiantyn Korchuhanov
 * @version 1.0
 * Note: This code was written by a genius (me) and a cup of coffee. If it doesn't work, please blame the coffee, not me
 */
public class RegisterRepository {
    private final EntityManager entityManager;

    /**
     * @param entityManager JPA entity manager for DB operations
     */
    public RegisterRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Persists a new entity instance into the database.
     *
     * @param entity the object to be saved
     * @param <T>    the type of the entity
     */
    public <T> void save(T entity) {
        entityManager.persist(entity);
    }
}