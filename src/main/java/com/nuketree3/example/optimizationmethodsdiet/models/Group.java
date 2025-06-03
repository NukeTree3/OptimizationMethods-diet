package com.nuketree3.example.optimizationmethodsdiet.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Group {
    List<Product> products = new ArrayList<>();
    double sumCalories = 0;
    double sumProteins = 0;
    double sumCarbohydrates = 0;
    double sumFats = 0;

    public void addProduct(Product p) {
        products.add(p);
        sumCalories += p.getCalories();
        sumProteins += p.getProteins();
        sumCarbohydrates += p.getCarbohydrates();
        sumFats += p.getFats();
    }

    public void removeProduct(Product p) {
        products.remove(p);
        sumCalories -= p.getCalories();
        sumProteins -= p.getProteins();
        sumCarbohydrates -= p.getCarbohydrates();
        sumFats -= p.getFats();
    }
}
