package com.example.entity.user;


import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
//@MappedSuperclass
@Entity
@Inheritance(strategy = InheritanceType.JOINED)

public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String username;
    private String name;
    private String surname;
    private String email;
    @Column(length = 9)
    private String phoneNumber;
    private String passwordHash;

    public int getUserId(){
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + userId +
                ", username='" + name + '\'' +
                ", username='" + surname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + passwordHash + '\'' +
                ", email='" + email + '\'' +
                ", phoneNum='" + phoneNumber + '\'' +
                '}';
    }

}