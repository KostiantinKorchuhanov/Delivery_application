package org.example.deliveryweb.repository;

import com.example.entity.user.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {

    Optional<Driver> findDriverByUsernameOrEmail(String username, String email);
}