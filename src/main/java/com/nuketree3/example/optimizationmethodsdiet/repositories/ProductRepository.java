package com.nuketree3.example.optimizationmethodsdiet.repositories;

import com.nuketree3.example.optimizationmethodsdiet.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT * FROM products", nativeQuery = true)
    List<Product> getAll();
}
