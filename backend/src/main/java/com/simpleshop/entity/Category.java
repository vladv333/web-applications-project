package com.simpleshop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// category entity - maps to categories table in db
@Entity
@Table(name = "categories")
@Data           // lombok generates getters, setters, tostring etc
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;

    private String name;
}
