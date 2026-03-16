package com.example.service;

import com.example.config.HibernateConfig;
import com.example.entity.user.User;
import com.example.repository.AdminRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

public class AdminService {
    public <T extends User> List<T> allUsers(Class<T> userClass){
        EntityManager entityManager = HibernateConfig.getEntityManager();
        try {
            AdminRepository adminRepository = new AdminRepository(entityManager);
            return adminRepository.takeUsers(userClass);
        }finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }
    public void updateUser(User user){
        EntityManager entityManager = HibernateConfig.getEntityManager();
        try {
            AdminRepository adminRepository = new AdminRepository(entityManager);
            adminRepository.updateUser(user);
        }
        finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }
    public void deleteUser(String email){
        EntityManager entityManager = HibernateConfig.getEntityManager();
        try {
            AdminRepository adminRepository = new AdminRepository(entityManager);
            User user = adminRepository.findUserByEmail(email);
            if (user != null){
                adminRepository.deleteUser(user);
            }
        }
        finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    public List<User> searchUsers(String searchText) {
        EntityManager entityManager = HibernateConfig.getEntityManager();
        try {
            AdminRepository adminRepository = new AdminRepository(entityManager);
            return adminRepository.searchUsers(searchText);
        }
        finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }
}
