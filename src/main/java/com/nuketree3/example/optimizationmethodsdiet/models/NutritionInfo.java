package com.nuketree3.example.optimizationmethodsdiet.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NutritionInfo{
    private double proteins;
    private double fats;
    private double carbs;
    private double calories;
}