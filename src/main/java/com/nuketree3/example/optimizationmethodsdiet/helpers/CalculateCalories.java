package com.nuketree3.example.optimizationmethodsdiet.helpers;

import com.nuketree3.example.optimizationmethodsdiet.emuns.Gender;
import com.nuketree3.example.optimizationmethodsdiet.emuns.UserActivityType;
import com.nuketree3.example.optimizationmethodsdiet.models.UserParam;

public class CalculateCalories {

    public static double calculateCalories(UserParam userParam) {
        if (userParam.getGender().equals(Gender.MALE)) {
            return ((6.5 * userParam.getUserHeight()) + (10 * userParam.getUserWeight()) - (5 * userParam.getUserAge()) + 5) * coefficientOfPhysicalActivity(userParam.getActivityType());
        } else {
            return ((6.5 * userParam.getUserHeight()) + (10 * userParam.getUserWeight()) - (5 * userParam.getUserAge()) - 161) * coefficientOfPhysicalActivity(userParam.getActivityType());
        }
    }

    public static double coefficientOfPhysicalActivity(UserActivityType activityType) {
        switch (activityType) {
            case VERY_LOW_ACTIVITY -> {
                return 1.2;
            }
            case LOW_ACTIVITY -> {
                return 1.375;
            }
            case MEDIUM_ACTIVITY -> {
                return 1.550;
            }
            case HIGH_ACTIVITY -> {
                return 1.725;
            }
            case VERY_HIGH_ACTIVITY -> {
                return 1.9;
            }
        }
        return 0;
    }

    public static double calculateProteins(double calories) {
        return (calories * 0.15) / 4;
    }

    public static double calculateFats(double calories) {
        return (calories * 0.3) / 9;
    }

    public static double calculateCarbs(double calories) {
        return (calories * 0.55) / 4;
    }
}
