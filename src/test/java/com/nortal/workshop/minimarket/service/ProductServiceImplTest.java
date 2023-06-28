package com.nortal.workshop.minimarket.service;

import com.nortal.workshop.minimarket.enums.ProductCategory;
import com.nortal.workshop.minimarket.model.Product;
import com.nortal.workshop.minimarket.repository.ProductRepository;
import com.nortal.workshop.minimarket.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ProductServiceImplTest {

  @Mock
  private ProductRepository productRepository;
  @InjectMocks
  private ProductService productService = new ProductServiceImpl();

  @Test
  void findProducts_byCategoryAndMaxPrice() {
    // Find by category and max price
    Long productId = 1L;
    String category = ProductCategory.FRUITS.name();
    Double maxPrice = 5.8;

    Product product1 = createProduct(productId);
    Mockito.when(productRepository.findByCategoryAndPriceIsLessThanEqual(category, maxPrice))
        .thenReturn(Collections.singletonList(product1));

    List<Product> result = productService.findProducts(category, maxPrice);
    Assertions.assertNotNull(result);
    validateProductsAreEqual(product1, result);
  }

  @Test
  void findProducts_byMaxPrice(){
    // Find by max price
    Long productId = 2L;
    String category = null;
    Double maxPrice = 5.8;
    Product product2 = createProduct(productId);
    Mockito.when(productRepository.findByPriceIsLessThanEqual(maxPrice))
        .thenReturn(Collections.singletonList(product2));

    List<Product> result = productService.findProducts(category, maxPrice);
    Assertions.assertNotNull(result);
    validateProductsAreEqual(product2, result);
  }

  @Test
  void findProducts_all(){
    // Find all
    Long productId = 3L;
    String category = null;
    Double maxPrice = null;
    Product product3 = createProduct(productId);
    Mockito.when(productRepository.findAll()).thenReturn(Collections.singletonList(product3));

    List<Product> result = productService.findProducts(category, maxPrice);
    Assertions.assertNotNull(result);
    validateProductsAreEqual(product3, result);
  }

  private static void validateProductsAreEqual(Product product, List<Product> result) {
    Assertions.assertEquals(product.getId(), result.get(0).getId());
    Assertions.assertEquals(product.getName(), result.get(0).getName());
    Assertions.assertEquals(product.getPrice(), result.get(0).getPrice());
    Assertions.assertEquals(product.getCategory(), result.get(0).getCategory());
    Assertions.assertEquals(product.getDescription(), result.get(0).getDescription());
  }

  private static Product createProduct(Long productId) {
    Product product = new Product();
    product.setId(productId);
    product.setName("Banana");
    product.setPrice(2.5);
    product.setCategory(ProductCategory.FRUITS.name());
    product.setDescription("Yummy");
    return product;
  }
}
