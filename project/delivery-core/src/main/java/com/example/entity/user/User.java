package com.example.entity.user;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


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