package com.nortal.workshop.minimarket.service;

import com.nortal.workshop.minimarket.model.Product;
import com.nortal.workshop.minimarket.model.rest.ProductDTO;

import java.util.List;

public interface ProductService {

  List<ProductDTO> getAll();

  List<Product> findProducts(String category, Double maxPrice);

  void saveOrUpdate(Product product);

}
