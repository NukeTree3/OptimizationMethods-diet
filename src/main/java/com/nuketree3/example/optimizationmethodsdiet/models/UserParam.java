package com.nuketree3.example.optimizationmethodsdiet.models;

import com.nuketree3.example.optimizationmethodsdiet.emuns.Gender;
import com.nuketree3.example.optimizationmethodsdiet.emuns.Target;
import com.nuketree3.example.optimizationmethodsdiet.emuns.UserActivityType;
import lombok.Data;

@Data
public class UserParam {
    private int userAge;
    private double userWeight;
    private double userHeight;
    private Gender gender;
    private UserActivityType activityType;
    private Target target;
    private int day;
    private int countOfEatings;
}
