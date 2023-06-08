package com.nortal.workshop.minimarket.service.impl;

import com.nortal.workshop.minimarket.model.Product;
import com.nortal.workshop.minimarket.repository.ProductRepository;
import com.nortal.workshop.minimarket.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("productService")
public class ProductServiceImpl implements ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Override
  public List<Product> getAll() {
    return productRepository.findAll();
  }

  @Override
  public Product get(Long productId) {
    return productRepository.getReferenceById(productId);
  }

  @Override
  public List<Product> findProducts(String category, Double maxPrice) {
    if (StringUtils.isEmpty(category)) {
      if (maxPrice != null) {
        return productRepository.findByPriceIsLessThanEqual(maxPrice);
      }
      return getAll();
    }
    return productRepository.findByCategoryAndPriceIsLessThanEqual(category, maxPrice);
  }

  @Override
  public void saveOrUpdate(Product product) {
    productRepository.save(product);
  }

  @Override
  public Product findProduct(Long productId, String productName, Double maxPrice) {
    Product product;
    if (maxPrice != null) {
      product = productRepository.findByIdAndNameContainingIgnoreCaseAndPriceIsLessThanEqual(
          productId,
          StringUtils.defaultIfEmpty(productName, "").trim(),
          maxPrice);
    } else {
      product = productRepository.findByIdAndNameContainingIgnoreCase(
          productId,
          StringUtils.defaultIfEmpty(productName, "").trim());
    }
    return product;
  }

}
