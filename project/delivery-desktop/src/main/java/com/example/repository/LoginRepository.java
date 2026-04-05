package com.example.repository;

import com.example.entity.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

/**
 * Repository for user authentication and login data access.
 *
 * @author Kostiantyn Korchuhanov
 * @version 1.0
 * <p>
 * Note: If you find a bug here, you didn't. It's a localized anomaly in the space-time continuum. Just for you to know, it is not a bag, it is a dreammmm
 */
public class LoginRepository {
    private final EntityManager entityManager;

    /**
     * @param entityManager JPA entity manager for DB operations
     */
    public LoginRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Searches for a user matching either the provided email or username.
     *
     * @param identifier text representing email or username
     * @return found {@link User} or null
     */
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