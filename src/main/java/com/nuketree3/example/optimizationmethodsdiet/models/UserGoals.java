package com.nuketree3.example.optimizationmethodsdiet.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserGoals {
    private double targetCalories;
    private double targetProtein;
    private double targetFats;
    private double targetCarbs;
    private double caloriesTolerance;
    private double proteinTolerance;
    private double fatsTolerance;
    private double carbsTolerance;

    public boolean isWithinTolerance(NutritionInfo info) {
        return Math.abs(info.getCalories() - targetCalories) <= caloriesTolerance &&
                Math.abs(info.getProteins() - targetProtein) <= proteinTolerance &&
                Math.abs(info.getFats() - targetFats) <= fatsTolerance &&
                Math.abs(info.getCarbs() - targetCarbs) <= carbsTolerance;
    }
}