package com.example.repository;

import com.example.config.HibernateConfig;
import com.example.entity.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class LoginRepository {
    EntityManager entityManager = HibernateConfig.getEntityManager();
    public LoginRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public User findUserByEmail(String email) {
        try{
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root).where(builder.equal(root.get("email"), email));
            TypedQuery<User> typedQuery = entityManager.createQuery(query);
            User user = typedQuery.getSingleResult();
            return user;
        }
        catch(Exception e){
            return null;
        }
    }
}
