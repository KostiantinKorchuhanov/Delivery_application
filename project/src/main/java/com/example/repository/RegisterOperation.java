package com.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class RegisterOperation {
    public <T> void save(T entity) {
        EntityManager entityManager = com.example.config.HibernateConfig.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            if(transaction.isActive()) transaction.rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }
}
