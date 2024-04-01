package com.example.Thymeleaf_demo.Services;

import com.example.Thymeleaf_demo.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductsRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(Long id);
}
