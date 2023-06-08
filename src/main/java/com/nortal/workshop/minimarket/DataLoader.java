package com.nortal.workshop.minimarket;

import com.nortal.workshop.minimarket.enums.ProductCategory;
import com.nortal.workshop.minimarket.model.Product;
import com.nortal.workshop.minimarket.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {
  private ProductRepository repository;

  @Autowired
  public DataLoader(ProductRepository repository) {
    this.repository = repository;
  }

  @Override
  public void run(ApplicationArguments args) {
    repository.save(new Product(1L, "Apple juice", 15.79, ProductCategory.DRINKS.name(), "100% natural juice"));
    repository.save(new Product(2L, "Orange juice", 10.79, ProductCategory.DRINKS.name(), "100% natural juice"));
    repository.save(new Product(3L, "Sprite", 35.59, ProductCategory.DRINKS.name(), null));
    repository.save(new Product(4L, "Fanta", 25.59, ProductCategory.DRINKS.name(), null));
    repository.save(new Product(5L, "Lollipop", 46.2, ProductCategory.SWEETS.name(), "Different colors"));
    repository.save(new Product(6L, "Chocolate", 67.59, ProductCategory.SWEETS.name(), "Milk chocolate with almonds"));
    repository.save(new Product(7L, "Apple", 90.35, ProductCategory.FRUITS.name(), null));
    repository.save(new Product(8L, "Orange", 25.4, ProductCategory.FRUITS.name(), "(Egypt)"));
    repository.save(new Product(9L, "Kiwi", 78.5, ProductCategory.FRUITS.name(), "(Greece)"));
    repository.save(new Product(10L, "Pear", 9.3, ProductCategory.FRUITS.name(), null));
    repository.save(new Product(11L, "Banana", 24.35, ProductCategory.FRUITS.name(), "(Panama)"));
    repository.save(new Product(12L, "Potato", 11.45, ProductCategory.VEGETABLES.name(), null));
    repository.save(new Product(13L, "Carrot", 36.59, ProductCategory.VEGETABLES.name(), null));
    repository.save(new Product(14L, "Broccoli", 78.75, ProductCategory.VEGETABLES.name(), "(Italy)"));
    repository.save(new Product(15L, "Onion", 6.25, ProductCategory.VEGETABLES.name(), null));
    repository.save(new Product(16L, "Tomato", 11.2, ProductCategory.VEGETABLES.name(), null));
  }
}
