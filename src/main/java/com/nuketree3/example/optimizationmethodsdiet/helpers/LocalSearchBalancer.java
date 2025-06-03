package com.nuketree3.example.optimizationmethodsdiet.helpers;

import com.nuketree3.example.optimizationmethodsdiet.models.Group;
import com.nuketree3.example.optimizationmethodsdiet.models.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class LocalSearchBalancer {



    private final List<Product> products;
    private final int nGroups;
    private final double targetCalories;
    private final double targetProteins;
    private final double targetCarbohydrates;
    private final double targetFats;

    public LocalSearchBalancer(HashMap<Product, Double> selectedQuantitiesMap, int nGroups) {

        List<Product> products = new ArrayList<>();
        for (Product p : selectedQuantitiesMap.keySet()) {
            double quantities = selectedQuantitiesMap.get(p);
            products.add(new Product(p.getId(), p.getName(), selectedQuantitiesMap.get(p),
                    (p.getCalories()/100) * quantities,
                    (p.getProteins()/100) * quantities,
                    (p.getCarbohydrates()/100) * quantities,
                    (p.getFats()/100) * quantities,
                    quantities*p.getPrice()/100));
        }

        this.products = products;
        this.nGroups = nGroups;

        double totalCalories = 0, totalProteins = 0, totalCarbs = 0, totalFats = 0;
        for (Product p : products) {
            totalCalories += p.getCalories();
            totalProteins += p.getProteins();
            totalCarbs += p.getCalories();
            totalFats += p.getFats();
        }
        targetCalories = totalCalories / nGroups;
        targetProteins = totalProteins / nGroups;
        targetCarbohydrates = totalCarbs / nGroups;
        targetFats = totalFats / nGroups;
    }

    public LocalSearchBalancer(List<Product> products, int nGroups) {

        this.products = products;
        this.nGroups = nGroups;

        double totalCalories = 0, totalProteins = 0, totalCarbs = 0, totalFats = 0;
        for (Product p : products) {
            totalCalories += p.getCalories();
            totalProteins += p.getProteins();
            totalCarbs += p.getCalories();
            totalFats += p.getFats();
        }
        targetCalories = totalCalories / nGroups;
        targetProteins = totalProteins / nGroups;
        targetCarbohydrates = totalCarbs / nGroups;
        targetFats = totalFats / nGroups;
    }

    private double calculateTotalImbalance(List<Group> groups) {
        double imbalance = 0;
        for (Group g : groups) {
            imbalance += Math.abs(g.getSumCalories() - targetCalories);
            imbalance += Math.abs(g.getSumProteins() - targetProteins);
            imbalance += Math.abs(g.getSumCarbohydrates() - targetCarbohydrates);
            imbalance += Math.abs(g.getSumFats() - targetFats);
        }
        return imbalance;
    }

    private List<Group> initialPartition() {
        List<Group> groups = new ArrayList<>();
        for (int i = 0; i < nGroups; i++) {
            groups.add(new Group());
        }

        List<Product> sorted = new ArrayList<>(products);
        sorted.sort((p1, p2) -> Double.compare(
                (p2.getCalories() + p2.getProteins() + p2.getCarbohydrates() + p2.getFats()),
                (p1.getCalories() + p1.getProteins() + p1.getCarbohydrates() + p1.getFats())));

        for (Product p : sorted) {
            int bestGroupIndex = 0;
            double bestImbalance = Double.MAX_VALUE;

            for (int i = 0; i < nGroups; i++) {
                Group g = groups.get(i);
                g.addProduct(p);
                double imbalance = calculateTotalImbalance(groups);
                g.removeProduct(p);

                if (imbalance < bestImbalance) {
                    bestImbalance = imbalance;
                    bestGroupIndex = i;
                }
            }
            groups.get(bestGroupIndex).addProduct(p);
        }

        return groups;
    }

    public List<Group> balance() {
        List<Group> groups = initialPartition();
        double currentImbalance = calculateTotalImbalance(groups);
        boolean improvement = true;
        int iteration = 0;
        int maxIterations = 1000;

        while (improvement && iteration < maxIterations) {
            improvement = false;
            iteration++;

            outer:
            for (int fromGroupIdx = 0; fromGroupIdx < nGroups; fromGroupIdx++) {
                Group fromGroup = groups.get(fromGroupIdx);

                for (Product p : new ArrayList<>(fromGroup.getProducts())) {
                    for (int toGroupIdx = 0; toGroupIdx < nGroups; toGroupIdx++) {
                        if (toGroupIdx == fromGroupIdx) continue;
                        Group toGroup = groups.get(toGroupIdx);

                        fromGroup.removeProduct(p);
                        toGroup.addProduct(p);

                        double newImbalance = calculateTotalImbalance(groups);

                        if (newImbalance < currentImbalance) {
                            currentImbalance = newImbalance;
                            improvement = true;
                            break outer;
                        } else {
                            toGroup.removeProduct(p);
                            fromGroup.addProduct(p);
                        }
                    }
                }
            }
        }

        return groups;
    }

    public List<Group> getGroups() {
        return balance();
    }
}
