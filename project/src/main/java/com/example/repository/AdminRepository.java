package com.example.repository;

import com.example.entity.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class AdminRepository {
    private final EntityManager entityManager;
    public AdminRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    public <T extends User> List<T> takeUsers(Class<T> userClass) {
        try{
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> query = builder.createQuery(userClass);
            Root<T> root = (Root<T>) query.from(userClass);
            query.select(root);
            return entityManager.createQuery(query).getResultList();
        }
        catch(Exception e){
            e.printStackTrace();
            return List.of();
        }
    }

    public void updateUser(User user) {
        try{
            entityManager.getTransaction().begin();
            entityManager.merge(user);
            entityManager.getTransaction().commit();
        }catch(Exception e){
            entityManager.getTransaction().rollback();
        }
    }

    public void deleteUser(User user) {
        try{
            entityManager.getTransaction().begin();
            if (entityManager.contains(user)){
                entityManager.remove(user);
            }
            else{
                User managedUser = entityManager.merge(user);
                entityManager.remove(managedUser);
            }
            entityManager.getTransaction().commit();
//            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//            CriteriaQuery<User> query = builder.createQuery(User.class);
//            Root<User> root = query.from(User.class);
//            query.select(root).where(builder.equal(root.get("email"), email));
//            TypedQuery<User> typedQuery = entityManager.createQuery(query);
//            User user = null;
//            try {
//                user = typedQuery.getSingleResult();
//            }
//            catch(jakarta.persistence.NoResultException e) {
//                entityManager.getTransaction().rollback();
//            }
//            entityManager.remove(user);
//            entityManager.getTransaction().commit();
        }
        catch(Exception e){
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    public User findUserByEmail(String email) {
        try{
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root).where(builder.equal(root.get("email"), email));
            return entityManager.createQuery(query).getSingleResult();
        }
        catch (Exception e){
            return null;
        }
    }

    public List<User> searchUsers(String searchText) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        String readyToSearch = "%" + searchText + "%";
        query.select(root).where(builder.or(
                builder.like(root.get("name"), readyToSearch),
                builder.like(root.get("email"), readyToSearch),
                builder.like(root.get("surname"), readyToSearch),
                builder.like(root.get("username"), readyToSearch)
        ));
        return entityManager.createQuery(query).getResultList();
    }
}
