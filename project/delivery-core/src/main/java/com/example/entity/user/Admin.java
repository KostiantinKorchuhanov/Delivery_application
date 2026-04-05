package com.example.entity.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Represents a specialized Administrator user in the system.
 * This entity extends the base {@link User} class and grants full
 * access to administrative functions like managing users and restaurants.
 *
 * @author Kostiantyn Korchuhanov
 * @version 1.0
 * @see User
 */
@Entity
@Table(name = "admin_table")
public class Admin extends User {
}
