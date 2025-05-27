package com.nuketree3.example.optimizationmethodsdiet.service;

import com.nuketree3.example.optimizationmethodsdiet.PointOptimizer;
import com.nuketree3.example.optimizationmethodsdiet.emuns.Gender;
import com.nuketree3.example.optimizationmethodsdiet.emuns.Target;
import com.nuketree3.example.optimizationmethodsdiet.emuns.UserActivityType;
import com.nuketree3.example.optimizationmethodsdiet.helpers.*;
import com.nuketree3.example.optimizationmethodsdiet.models.Product;
import com.nuketree3.example.optimizationmethodsdiet.models.UserParam;
import com.nuketree3.example.optimizationmethodsdiet.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    Optimizer optimizer;


    public void goSimplex(List<Product> productUnchoise, UserParam param) {
//        List<Product> products = productRepository.findAll();
//        products.removeAll(productUnchoise);
        optimizer.go(productUnchoise, param);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void go(){

        UserParam userParam = new UserParam();
        userParam.setUserAge(20);
        userParam.setUserWeight(76);
        userParam.setUserHeight(195);
        userParam.setGender(Gender.MALE);
        userParam.setActivityType(UserActivityType.LOW_ACTIVITY);
        userParam.setTarget(Target.WEIGHT_MAINTENANCE);
        List<Product> products = productRepository.findAll();
        System.out.println(products.size());
        goSimplex(products, userParam);
    }
}
