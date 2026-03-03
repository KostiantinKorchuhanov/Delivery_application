package com.example.model.operations;

import com.example.model.utils.HybernateUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class PerformRegistration {
    public <T> void save(T entity) {
        EntityManager entityManager = HybernateUtils.getEntityManager();
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
