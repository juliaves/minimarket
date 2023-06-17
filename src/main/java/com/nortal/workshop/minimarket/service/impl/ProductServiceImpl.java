package com.nortal.workshop.minimarket.service.impl;

import com.nortal.workshop.minimarket.exceptions.CustomException;
import com.nortal.workshop.minimarket.model.Product;
import com.nortal.workshop.minimarket.model.rest.ProductDTO;
import com.nortal.workshop.minimarket.repository.ProductRepository;
import com.nortal.workshop.minimarket.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("productService")
public class ProductServiceImpl implements ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Override
  public List<ProductDTO> getAll() {
    List<Product> products = productRepository.findAll();
    List<ProductDTO> productDTOs = new ArrayList<>();
    for (Product product : products) {
      ProductDTO productDTO = convertToProductDTO(product);
      productDTOs.add(productDTO);
    }
    return productDTOs;
  }

  private ProductDTO convertToProductDTO(Product product) {
    ProductDTO productDTO = new ProductDTO();
    productDTO.setId(product.getId());
    productDTO.setName(product.getName());
    productDTO.setPrice(product.getPrice());
    productDTO.setCategory(product.getCategory());
    productDTO.setDescription(product.getDescription());
    return productDTO;
  }

  @Override
  public List<Product> findProducts(String category, Double maxPrice) {
    if (StringUtils.isEmpty(category)) {
      if (maxPrice != null) {
        return productRepository.findByPriceIsLessThanEqual(maxPrice);
      }
      return productRepository.findAll();
    }
    return productRepository.findByCategoryAndPriceIsLessThanEqual(category, maxPrice);
  }

  @Override
  public void saveOrUpdate(Product product) {
    try {
      productRepository.save(product);
    } catch (Exception ex) {
      throw new CustomException("Something went wrong: " + ex.getMessage());
    }
  }

}
