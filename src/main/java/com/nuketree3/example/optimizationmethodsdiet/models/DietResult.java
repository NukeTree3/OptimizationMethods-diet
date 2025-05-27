package com.nuketree3.example.optimizationmethodsdiet.models;

import lombok.Data;

@Data
public class DietResult {
    private String[][] productsWithCountAndPrices;
    private String totalCost;
    private String totalCalories;
    private String totalProteins;
    private String totalFats;
    private String totalCarbohydrates;
}
