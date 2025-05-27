package com.nuketree3.example.optimizationmethodsdiet.controllers;

import com.nuketree3.example.optimizationmethodsdiet.models.Product;
import com.nuketree3.example.optimizationmethodsdiet.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/go")
    public void admin() {
        productService.go();
    }

    @GetMapping("/condition")
    public String UserConditions(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "condition";
    }

    @PostMapping("/result")
    public String getUserCondition(@RequestBody List<Product> products, Model model) {
        model.addAttribute("products", products);
        return "result";
    }
}
