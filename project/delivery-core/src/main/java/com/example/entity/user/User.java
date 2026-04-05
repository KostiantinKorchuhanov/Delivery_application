package com.example.entity.user;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Abstract base class representing a User in the system.
 * Uses the JOINED inheritance strategy to persist different user roles in separate tables.
 *
 * @author Kostiantyn Korchuhanov
 * @version 1.0
 * <p>
 * Since no one is actually going to read this anyway (because why would you?),
 * I’ve decided to sprinkle some jokes throughout my files.
 * That way, if some poor soul actually ends up reading this, at least you won't die of boredom
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "user_table")


public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    @Column(unique = true, nullable = false)
    private String username;
    private String name;
    private String surname;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(length = 9)
    private String phoneNumber;
    private String passwordHash;

    @Override
    public String toString() {
        return "User{" +
                "id=" + userId +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + passwordHash + '\'' +
                ", email='" + email + '\'' +
                ", phoneNum='" + phoneNumber + '\'' +
                '}';
    }
}