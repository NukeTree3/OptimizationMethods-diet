package com.nuketree3.example.optimizationmethodsdiet.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity
@Table(name = "products")
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "count")
    private double count;
    @Column(name = "calories")
    private double calories;
    @Column(name = "proteins")
    private double proteins;
    @Column(name = "carbohydrates")
    private double carbohydrates;
    @Column(name = "fats")
    private double fats;
    @Column(name = "price")
    private double price;

    public Product() {

    }
}
