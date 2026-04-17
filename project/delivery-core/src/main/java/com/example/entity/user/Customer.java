package com.example.entity.user;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "customer_table")

public class Customer extends User {
    private int bonusPoints = 0;
}

