package com.nuketree3.example.optimizationmethodsdiet.service;

import com.nuketree3.example.optimizationmethodsdiet.helpers.*;
import com.nuketree3.example.optimizationmethodsdiet.models.*;
import com.nuketree3.example.optimizationmethodsdiet.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    Optimizer optimizer;

    public DietResult getDietResult(DietPlan dietPlan) {
        return optimizer.formatResult(dietPlan);
    }

    public DietPlan getDietPlan(List<Product> productUnchoise, UserParam param){
        List<Product> products = productRepository.findAll();
        System.out.println(products.size());
        if(productUnchoise != null) {
            products.removeAll(productUnchoise);
        }
        System.out.println(products.size());
        //Optimizer.go(products);

        return optimizer.go(products, param);
    }

    public List<List<Group>> getGroups(DietPlan dietPlan, int days, int countOfEatings) {
        if (days == 1){
            LocalSearchBalancer localSearchBalancer = new LocalSearchBalancer(dietPlan.getSelectedQuantitiesMap(), countOfEatings);
            List<List<Group>> gr = new ArrayList<>();
            gr.add(localSearchBalancer.getGroups());
            return gr;
        }
        LocalSearchBalancer localSearchBalancer = new LocalSearchBalancer(dietPlan.getSelectedQuantitiesMap(), days);
        List<Group> group = localSearchBalancer.getGroups();
        List<List<Group>> groups = new ArrayList<>();
        for (int i = 0; i < group.size(); i++) {
            LocalSearchBalancer localSearchBalancerForOneDay = new LocalSearchBalancer(group.get(i).getProducts(), countOfEatings);
            groups.add(localSearchBalancerForOneDay.getGroups());
        }
        return groups;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public DietPlan go(List<Product> productUnchoise, UserParam param){
        return getDietPlan(productUnchoise, param);
    }
}
