package com.nuketree3.example.optimizationmethodsdiet.service;

import com.nuketree3.example.optimizationmethodsdiet.helpers.*;
import com.nuketree3.example.optimizationmethodsdiet.models.DietResult;
import com.nuketree3.example.optimizationmethodsdiet.models.Product;
import com.nuketree3.example.optimizationmethodsdiet.models.UserParam;
import com.nuketree3.example.optimizationmethodsdiet.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    Optimizer optimizer;



    public DietResult goSimplex(List<Product> productUnchoise, UserParam param) {
        List<Product> products = productRepository.findAll();
        System.out.println(products.size());
        if(productUnchoise != null) {
            products.removeAll(productUnchoise);
        }
        System.out.println(products.size());
        //Optimizer.go(products);
        return optimizer.go(products, param);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public DietResult go(List<Product> productUnchoise, UserParam param){
        return goSimplex(productUnchoise, param);
    }
}
