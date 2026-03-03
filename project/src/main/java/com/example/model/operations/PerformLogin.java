package com.example.model.operations;

import com.example.model.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class PerformLogin {
    private EntityManager entityManager;
    public PerformLogin(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public User findUserByEmail(String email) {
        try{
            TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        }
        catch(Exception e){
            return null;
        }
    }
}
