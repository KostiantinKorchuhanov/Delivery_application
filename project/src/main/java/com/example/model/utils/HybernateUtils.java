package com.example.model.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class HybernateUtils {
    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("deliveryApp");

    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public static void shutdown() {
        entityManagerFactory.close();
    }
}
