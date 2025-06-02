package com.nuketree3.example.optimizationmethodsdiet.controllers;

import com.nuketree3.example.optimizationmethodsdiet.emuns.Gender;
import com.nuketree3.example.optimizationmethodsdiet.emuns.Target;
import com.nuketree3.example.optimizationmethodsdiet.emuns.UserActivityType;
import com.nuketree3.example.optimizationmethodsdiet.models.Product;
import com.nuketree3.example.optimizationmethodsdiet.models.UserParam;
import com.nuketree3.example.optimizationmethodsdiet.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/go")
    public String admin(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("Target", Target.values());
        model.addAttribute("UserActivityType", UserActivityType.values());
        return "main";
    }

    @PostMapping("/result")
    public String dietResult(@ModelAttribute UserParam userParam, @RequestParam(value = "UncheckedProducts", required = false) List<Product> uncheckedProducts, Model model) {
        System.out.println(userParam.toString());
        model.addAttribute("result", productService.go(uncheckedProducts, userParam));
        return "result";
    }

    @GetMapping("/self")
    public String self() {
        return "selfproductresult";
    }

    @GetMapping("/")
    public String startPage(){
        return "hello";
    }

    @GetMapping("/all-prouducts")
    public String allProuducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "allprouducts";
    }
}
