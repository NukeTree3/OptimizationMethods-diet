package com.nuketree3.example.optimizationmethodsdiet.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;

@Data
@AllArgsConstructor
public class DietPlan {
    private HashMap<Product, Double> selectedQuantitiesMap;
    private double totalCost;
    private NutritionInfo nutrition;
}