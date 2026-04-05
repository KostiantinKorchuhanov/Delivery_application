package com.example.entity.user;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Represents a Customer user in the system.
 * This class extends the base {@link User} entity and inherits all common
 * user attributes like name, email, and password.
 *
 * @author Kostiantyn Korchuhanov
 * @version 1.0
 * @see User
 * <p>
 * Note: This class is 100% bug-free. The bugs are actually features in disguise
 */
@Entity
@Table(name = "customer_table")

public class Customer extends User {

}

