package com.nortal.workshop.minimarket.rest;

import com.nortal.workshop.minimarket.model.Product;
import com.nortal.workshop.minimarket.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/products")
public class ProductResource {

  @Autowired
  private ProductService productService;

  @GetMapping(produces = "application/json")
  public List<Product> getAllProducts() {
    return productService.getAll();
  }

  @GetMapping(path = "/find", produces = "application/json")
  public List<Product> findProducts(@RequestParam String category, @RequestParam Double maxPrice) {
    return productService.findProducts(category, maxPrice);
  }

  @PostMapping(consumes = "application/json")
  public ResponseEntity<String> save(@NonNull @RequestBody Product product) {
    if (!product.validate()) {
      return ResponseEntity.badRequest().build();
    }
    productService.saveOrUpdate(product);
    return ResponseEntity.ok().body("Product is successfully saved!");
  }
}
