package com.nortal.workshop.minimarket.repository;

import com.nortal.workshop.minimarket.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

  List<Product> findByPriceIsLessThanEqual(Double maxPrice);

  List<Product> findByCategoryAndPriceIsLessThanEqual(String category, Double maxPrice);

  Product findByIdAndNameContainingIgnoreCaseAndPriceIsLessThanEqual(Long productId, String productName, Double maxPrice);

  Product findByIdAndNameContainingIgnoreCase(Long productId, String productName);
}
