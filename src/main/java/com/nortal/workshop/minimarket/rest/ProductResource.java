package com.nortal.workshop.minimarket.rest;

import com.nortal.workshop.minimarket.model.Product;
import com.nortal.workshop.minimarket.model.rest.ProductDTO;
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
  public List<ProductDTO> getAllProducts() {
    return productService.getAll();
  }

  @GetMapping(path = "/find", produces = "application/json")
  public List<Product> findProducts(@RequestParam String category, @RequestParam Double maxPrice) {
    return productService.findProducts(category, maxPrice);
  }

  @PostMapping(consumes = "application/json")
  public ResponseEntity<Void> save(@NonNull @RequestBody ProductDTO productDTO) {
    if (!productDTO.validate()) {
      return ResponseEntity.badRequest().build();
    }
    Product product = new Product();
    product.setId(productDTO.getId());
    product.setName(productDTO.getName());
    product.setCategory(productDTO.getCategory());
    product.setPrice(productDTO.getPrice());
    product.setDescription(productDTO.getDescription());

    productService.saveOrUpdate(product);
    return ResponseEntity.ok().build();
  }
}
