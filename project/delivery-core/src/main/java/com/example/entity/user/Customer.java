package com.example.entity.user;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer_table")

public class Customer extends User {

}

