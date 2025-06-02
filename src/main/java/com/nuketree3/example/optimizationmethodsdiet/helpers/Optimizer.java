package com.nuketree3.example.optimizationmethodsdiet.helpers;

import com.nuketree3.example.optimizationmethodsdiet.emuns.Gender;
import com.nuketree3.example.optimizationmethodsdiet.emuns.UserActivityType;
import com.nuketree3.example.optimizationmethodsdiet.models.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class Optimizer {

    private static final double LAMBDA_CALORIES = 0.1;
    private static final double LAMBDA_PROTEIN = 0.1;
    private static final double LAMBDA_FATS = 0.1;
    private static final double LAMBDA_CARBS = 0.1;
    private static final double LAMBDA_DIVERSITY = 1.0;
    private static final double MIN_PRODUCT_AMOUNT = 60.0;
    private static double LAMBDA = 0.1;

    public DietResult go(List<Product> products, UserParam userParam) {

        int day = userParam.getDay();
        double calories = CalculateCalories.calculateCalories(userParam) * day;

        System.out.println("скока надо калорий " + calories);
        System.out.println("скока надо белков " + CalculateCalories.calculateProteins(calories));
        System.out.println("скока надо жиров " + CalculateCalories.calculateFats(calories));
        System.out.println("скока надо углеводов " + CalculateCalories.calculateCarbs(calories));

        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Список продуктов не может быть пустым");
        }

        DietPlan optimalPlan = optimize(products, userParam, day);

        if (optimalPlan.getProducts().isEmpty()) {
            System.out.println("Не удалось найти решение с текущими параметрами");
            return null;
        }

        if (!isWithinTolerance(optimalPlan.getNutrition(),
                new UserGoals(
                        calories,
                        CalculateCalories.calculateProteins(calories),
                        CalculateCalories.calculateFats(calories),
                        CalculateCalories.calculateCarbs(calories),
                        0.1, 0.1, 0.1, 0.1))) {
            System.out.println("Внимание: решение не полностью соответствует целям");
        }

        return formatResult(optimalPlan);
    }

    private void changeProductPenalty(double[] penalty, double[] cost, double[] xCurrent, double[] prices) {
        for (int i = 0; i < penalty.length; i++) {
            penalty[i] = 2 * LAMBDA * cost[i] * xCurrent[i];
            cost[i] = penalty[i] * prices[i];
        }
    }

    private double totalCost(double[] xCurrent, List<Product> products) {
        double sum = 0;
        for (int i = 0; i < products.size(); i++) {
            sum += xCurrent[i] * products.get(i).getPrice();
        }
        return sum;
    }

    public double computePenalties(NutritionInfo currentNutrition,
                                   UserGoals userGoals,
                                   double[] quantities) {
        double calories = currentNutrition.getCalories();

        double caloriePenalty =
                Math.pow(Math.max(0, userGoals.getCaloriesTolerance() - currentNutrition.getCalories()), 2) * LAMBDA_CALORIES +
                        LAMBDA_CALORIES * Math.pow(currentNutrition.getCalories() - userGoals.getTargetCalories(), 2);

        double proteinPenalty =
                Math.pow(Math.max(0, userGoals.getTargetProtein() - currentNutrition.getProteins()), 2) * LAMBDA_CALORIES +
                        LAMBDA_PROTEIN * Math.pow(currentNutrition.getProteins() - userGoals.getTargetProtein(), 2);

        double fatPenalty =
                Math.pow(Math.max(0, userGoals.getFatsTolerance() - currentNutrition.getFats()), 2) * LAMBDA_CALORIES +
                        LAMBDA_FATS * Math.pow(currentNutrition.getFats() - userGoals.getTargetFats(), 2);

        double carbsPenalty =
                Math.pow(Math.max(0, userGoals.getCarbsTolerance() - currentNutrition.getCarbs()), 2) * LAMBDA_CALORIES +
                        LAMBDA_CARBS * Math.pow(currentNutrition.getCarbs() - userGoals.getTargetCarbs(), 2);

        double diversityPenalty = computeDiversityPenalty(quantities);

        return caloriePenalty + proteinPenalty + fatPenalty + carbsPenalty
                + LAMBDA_DIVERSITY * diversityPenalty;
    }


    private double computeDiversityPenalty(double[] quantities) {
        double avg = Arrays.stream(quantities).average().orElse(0);
        double penalty = 0;

        for (double q : quantities) {
            penalty += Math.pow(q - avg, 2);
        }

        return penalty;
    }

    private double[] initialProductsPrices(List<Product> products) {
        double[] prices = new double[products.size()];
        for (int i = 0; i < prices.length; i++) {
            prices[i] = products.get(i).getPrice();
        }
        return prices;
    }

    public double[] calculateInitialGuess(int length) {
        double[] approximation = new double[length];
        Random random = new Random();
        for (int i = 0; i < approximation.length; i++) {
            approximation[i] = random.nextDouble(50.0, 100.0);
        }

        return approximation;
    }

    public double[][] createNutrientsTable(List<Product> products) {
        double[][] nutrientsTable = new double[4][products.size()];
        for (int i = 0; i < products.size(); i++) {
            nutrientsTable[0][i] = products.get(i).getProteins();
        }
        for (int i = 0; i < products.size(); i++) {
            nutrientsTable[1][i] = products.get(i).getFats();
        }
        for (int i = 0; i < products.size(); i++) {
            nutrientsTable[2][i] = products.get(i).getCarbohydrates();
        }
        for (int i = 0; i < products.size(); i++) {
            nutrientsTable[3][i] = products.get(i).getCalories();
        }
        return nutrientsTable;
    }



    private NutritionInfo nutritionInfo(double[] xCurrent, List<Product> products){
        double proteins = 0.0;
        double fats = 0.0;
        double carbs = 0.0;
        double calories = 0.0;
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            double quantity = xCurrent[i];

            if (quantity <= 0) continue;

            double coefficient = quantity / 100.0;

            proteins += product.getProteins() * coefficient;
            fats += product.getFats() * coefficient;
            carbs += product.getCarbohydrates() * coefficient;
            calories += product.getCalories() * coefficient;
        }
        return new NutritionInfo(proteins, fats, carbs, calories);
    }

    public DietPlan optimize(List<Product> products, UserParam userParam, int day) {
        double[] xCurrent = calculateInitialGuess(products.size());
        /*
            private static final double LAMBDA_CALORIES = 0.1;
    private static final double LAMBDA_PROTEIN = 0.2;
    private static final double LAMBDA_FATS = 0.1;
    private static final double LAMBDA_CARBS = 0.1;
    private static final double LAMBDA_DIVERSITY = 0.05;
         */
        double targetCalories = CalculateCalories.calculateCalories(userParam) * day;

        UserGoals goals = new UserGoals(
                targetCalories,
                CalculateCalories.calculateProteins(targetCalories),
                CalculateCalories.calculateFats(targetCalories),
                CalculateCalories.calculateCarbs(targetCalories),
                0.1,0.1,0.1,0.1);
        double learningRate = 0.0001;
        int maxIterations = 2000;
        double prevScore = Double.MAX_VALUE;


        double temperature = 1.0;
        double coolingRate = 0.999;

        for (int iter = 0; iter < maxIterations; iter++) {
            System.out.println(iter);
            NutritionInfo currentNutrition = nutritionInfo(xCurrent, products);
            double currentScore = totalCost(xCurrent, products) + computePenalties(currentNutrition, goals, xCurrent);

            if (isWithinTolerance(currentNutrition, goals)) {
                break;
            }

            if (iter > 10 && Math.abs(prevScore - currentScore) < 1e-5) {
                goals = adjustConstraints(goals, currentNutrition, 0.05);
                learningRate = adjustLearningRate(currentScore, prevScore, learningRate, 0.9);
            }

            prevScore = currentScore;
                double[] gradient = computeGradient(xCurrent, products, goals);

            for (int i = 0; i < xCurrent.length; i++) {
                xCurrent[i] -= learningRate * gradient[i];
                xCurrent[i] = Math.max(xCurrent[i], 0);
            }

            if (iter % 100 == 0) {
                for (int i = 0; i < xCurrent.length; i++) {
                    if (xCurrent[i] > 0) {
                        xCurrent[i] *= (1 + (Math.random() - 0.5) * temperature);
                    }
                }
            }
        }
        return createDietPlan(xCurrent, products);
    }

    private boolean isWithinTolerance(NutritionInfo nutrition, UserGoals goals) {
        return Math.abs(nutrition.getCalories() - goals.getTargetCalories()) <= goals.getCaloriesTolerance() &&
                Math.abs(nutrition.getProteins() - goals.getTargetProtein()) <= goals.getProteinTolerance() &&
                Math.abs(nutrition.getFats() - goals.getFatsTolerance()) <= goals.getFatsTolerance() &&
                Math.abs(nutrition.getCarbs() - goals.getCarbsTolerance()) <= goals.getCarbsTolerance();
    }

    private double[] computeGradient(double[] xCurrent, List<Product> products, UserGoals goals) {
        double[] gradient = new double[xCurrent.length];
        double delta = 1e-5;
        for (int i = 0; i < xCurrent.length; i++) {
            double original = xCurrent[i];

            xCurrent[i] = original + delta;
            NutritionInfo plus = nutritionInfo(xCurrent, products);
            double scorePlus = totalCost(xCurrent, products) + computePenalties(plus, goals, xCurrent);

            xCurrent[i] = original - delta;
            NutritionInfo minus = nutritionInfo(xCurrent, products);
            double scoreMinus = totalCost(xCurrent, products) + computePenalties(minus, goals, xCurrent);

            xCurrent[i] = original;

            gradient[i] = (scorePlus - scoreMinus) / (2 * delta);
        }

        return gradient;
    }

    private DietPlan createDietPlan(double[] quantities, List<Product> products) {
//        List<Product> selectedProducts = new ArrayList<>();
//        List<Double> selectedQuantities = new ArrayList<>();
//
//        for (int i = 0; i < quantities.length; i++) {
//            if (quantities[i] > 1e-3) { // Игнорируем очень малые количества
//                selectedProducts.add(products.get(i));
//                selectedQuantities.add(quantities[i]);
//            }
//        }
//
//        NutritionInfo nutrition = nutritionInfo(quantities, products);
//        double cost = totalCost(quantities, products);
//
//        return new DietPlan(selectedProducts, selectedQuantities, cost, nutrition);

        List<Product> selectedProducts = new ArrayList<>();
        List<Double> selectedQuantities = new ArrayList<>();

        // Сначала выбираем только продукты с достаточным количеством
        for (int i = 0; i < quantities.length; i++) {
            if (quantities[i] >= MIN_PRODUCT_AMOUNT) {
                selectedProducts.add(products.get(i));
                selectedQuantities.add(quantities[i]);
            }
        }

        if (selectedProducts.size() < 5) {
            List<Integer> indices = IntStream.range(0, quantities.length)
                    .boxed()
                    .sorted((i, j) -> Double.compare(quantities[j], quantities[i]))
                    .collect(Collectors.toList());

            for (int i : indices) {
                if (quantities[i] > 1e-3 && !selectedProducts.contains(products.get(i))) {
                    selectedProducts.add(products.get(i));
                    selectedQuantities.add(Math.max(quantities[i], MIN_PRODUCT_AMOUNT));
                    if (selectedProducts.size() >= 10) break;
                }
            }
        }

        NutritionInfo nutrition = nutritionInfo(
                selectedProducts.stream().mapToDouble(p ->
                        selectedQuantities.get(selectedProducts.indexOf(p))
                ).toArray(),
                selectedProducts
        );

        double cost = totalCost(
                selectedProducts.stream().mapToDouble(p ->
                        selectedQuantities.get(selectedProducts.indexOf(p))
                ).toArray(),
                selectedProducts
        );

        return new DietPlan(selectedProducts, selectedQuantities, cost, nutrition);
    }

    public UserGoals adjustConstraints(UserGoals goals,
                                       NutritionInfo currentNutrition,
                                       double initialTolerance) {
        final double expansionFactor = 1.05;

        double caloriesDeviation = Math.abs(currentNutrition.getCalories() - goals.getTargetCalories());
        double proteinDeviation = Math.abs(currentNutrition.getProteins() - goals.getTargetProtein());
        double fatsDeviation = Math.abs(currentNutrition.getFats() - goals.getTargetFats());
        double carbDeviation = Math.abs(currentNutrition.getCarbs() - goals.getTargetCarbs());

        double newCaloriesTolerance = (caloriesDeviation + initialTolerance) * expansionFactor;
        double newProteinTolerance = (proteinDeviation + initialTolerance) * expansionFactor;
        double newFatsTolerance = (fatsDeviation + initialTolerance) * expansionFactor;
        double newCarbsTolerance = (carbDeviation + initialTolerance) * expansionFactor;

        return new UserGoals(
                goals.getTargetCalories(),
                goals.getTargetProtein(),
                goals.getTargetFats(),
                goals.getTargetCarbs(),
                newCaloriesTolerance,
                newProteinTolerance,
                newFatsTolerance,
                newCarbsTolerance
        );
    }


    public double adjustLearningRate(double currentScore,
                                     double prevScore,
                                     double learningRate,
                                     double adjustmentFactor) {
        if (currentScore >= prevScore) {
            return learningRate * adjustmentFactor;
        }
        return learningRate;
    }

    public DietResult formatResult(DietPlan plan) {
        DietResult result = new DietResult();

        String[][] strings = new String[plan.getProducts().size()][3];
        for (int i = 0; i < plan.getProducts().size(); i++) {
            Product p = plan.getProducts().get(i);
            double quantity = plan.getQuantities().get(i);
            String[] productParam = new String[3];
            productParam[0] = p.getName();
            productParam[1] = String.format("%.2f г", quantity);
            productParam[2] = String.format("%.2f руб", quantity*p.getCalories()/100);
            strings[i] = productParam;
        }
        result.setProductsWithCountAndPrices(strings);

        result.setTotalCost(String.format("%.2f руб", plan.getTotalCost()));
        result.setTotalCalories(String.format("%.2f ккал", plan.getNutrition().getCalories()));
        result.setTotalProteins(String.format("%.2f г", plan.getNutrition().getProteins()));
        result.setTotalFats(String.format("%.2f г", plan.getNutrition().getFats()));
        result.setTotalCarbohydrates(String.format("%.2f г", plan.getNutrition().getCarbs()));

        return result;
    }


    @Data
    @AllArgsConstructor
    public class DietPlan {
        private List<Product> products;
        private List<Double> quantities;
        private double totalCost;
        private NutritionInfo nutrition;
    }
}