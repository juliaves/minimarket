package com.nortal.workshop.minimarket.service;

import com.nortal.workshop.minimarket.model.Product;

import java.util.List;

public interface ProductService {

  List<Product> getAll();

  List<Product> findProducts(String category, Double maxPrice);

  void saveOrUpdate(Product product);

}
