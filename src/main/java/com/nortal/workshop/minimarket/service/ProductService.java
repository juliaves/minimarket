package com.nortal.workshop.minimarket.service;

import com.nortal.workshop.minimarket.model.Product;

import java.util.List;

public interface ProductService {

  List<Product> getAll();

  Product get(Long productId);

  List<Product> findProducts(String category, Double maxPrice);

  void saveOrUpdate(Product product);

  Product findProduct(Long productId, String productName, Double maxPrice);
}
