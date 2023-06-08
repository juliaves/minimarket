package com.nortal.workshop.minimarket.service.impl;

import com.nortal.workshop.minimarket.model.CartProduct;
import com.nortal.workshop.minimarket.model.Product;
import com.nortal.workshop.minimarket.model.Purchase;
import com.nortal.workshop.minimarket.model.PurchaseFilterParams;
import com.nortal.workshop.minimarket.model.PurchaseProduct;
import com.nortal.workshop.minimarket.model.PurchaseWithProducts;
import com.nortal.workshop.minimarket.repository.PurchaseProductRepository;
import com.nortal.workshop.minimarket.repository.PurchaseRepository;
import com.nortal.workshop.minimarket.service.ProductService;
import com.nortal.workshop.minimarket.service.PurchaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service("purchaseService")
public class PurchaseServiceImpl implements PurchaseService {
  @Autowired
  private PurchaseRepository purchaseRepository;
  @Autowired
  private PurchaseProductRepository purchaseProductRepository;
  @Autowired
  private ProductService productService;

  @Override
  public void save(PurchaseWithProducts purchaseWithProducts) {
    Purchase purchase = new Purchase();
    purchase.setDate(new Date());
    purchase.setFirstName(purchaseWithProducts.getFirstName());
    purchase.setLastName(purchaseWithProducts.getLastName());
    purchase.setEmail(purchaseWithProducts.getEmail());
    purchase = purchaseRepository.save(purchase);

    List<PurchaseProduct> purchaseProducts = new ArrayList<>();
    for (CartProduct product : purchaseWithProducts.getCartProducts()) {
      PurchaseProduct purchaseProduct = new PurchaseProduct();
      purchaseProduct.setProductId(product.getId());
      purchaseProduct.setQuantity(product.getQuantity());
      purchaseProduct.setPurchase(purchase);
      purchaseProducts.add(purchaseProduct);
    }
    purchaseProductRepository.saveAll(purchaseProducts);
  }

  @Override
  public List<PurchaseWithProducts> getAll() {
    List<Purchase> allPurchases = purchaseRepository.findAll();
    List<PurchaseWithProducts> purchasesWithProducts = new ArrayList<>();
    if (CollectionUtils.isEmpty(allPurchases)) {
      return purchasesWithProducts;
    }
    allPurchases.forEach(purchase -> {
      PurchaseWithProducts purchaseWithProducts = new PurchaseWithProducts();
      purchaseWithProducts.setId(purchase.getId());
      purchaseWithProducts.setDate(purchase.getDate());
      purchaseWithProducts.setEmail(purchase.getEmail());
      purchaseWithProducts.setFirstName(purchase.getFirstName());
      purchaseWithProducts.setLastName(purchase.getLastName());

      List<CartProduct> cartProducts = new ArrayList<>();
      List<PurchaseProduct> purchaseProducts = purchaseProductRepository.findByPurchaseId(purchase.getId());
      purchaseProducts.forEach(purchaseProduct -> {
        CartProduct cartProduct = new CartProduct();
        Product product = productService.get(purchaseProduct.getProductId());
        cartProduct.setId(product.getId());
        cartProduct.setCategory(product.getCategory());
        cartProduct.setName(product.getName());
        cartProduct.setPrice(product.getPrice());
        cartProduct.setDescription(product.getDescription());
        cartProduct.setQuantity(purchaseProduct.getQuantity());
        cartProducts.add(cartProduct);
      });
      purchaseWithProducts.setCartProducts(cartProducts);
      purchasesWithProducts.add(purchaseWithProducts);
    });
    return purchasesWithProducts;
  }

  @Override
  public List<PurchaseWithProducts> searchPurchases(PurchaseFilterParams searchParams) {
    List<Purchase> purchases = purchaseRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(
        StringUtils.defaultIfEmpty(searchParams.getFirstName(), "").trim(),
        StringUtils.defaultIfEmpty(searchParams.getLastName(), "").trim());
    if (CollectionUtils.isEmpty(purchases)) {
      return Collections.emptyList();
    }
    List<PurchaseWithProducts> result = new ArrayList<>();
    for (Purchase purchase : purchases) {
      List<PurchaseProduct> purchaseProducts;
      if (searchParams.getMaxQuantity() == null) {
        purchaseProducts = purchaseProductRepository.findByPurchaseId(purchase.getId());
      } else {
        purchaseProducts = purchaseProductRepository.findByPurchaseIdAndQuantityLessThanEqual(
            purchase.getId(),
            searchParams.getMaxQuantity());
      }
      if (CollectionUtils.isEmpty(purchaseProducts)) {
        continue;
      }
      PurchaseWithProducts purchaseWithProducts = new PurchaseWithProducts();
      purchaseWithProducts.setId(purchase.getId());
      purchaseWithProducts.setFirstName(purchase.getFirstName());
      purchaseWithProducts.setLastName(purchase.getLastName());
      purchaseWithProducts.setEmail(purchase.getEmail());
      purchaseWithProducts.setDate(purchase.getDate());

      List<CartProduct> cartProducts = new ArrayList<>();
      for (PurchaseProduct purchaseProduct : purchaseProducts) {
        Product product = productService.findProduct(
            purchaseProduct.getProductId(),
            searchParams.getProductName(),
            searchParams.getMaxPrice());
        if (product == null) {
          continue;
        }
        CartProduct cartProduct = new CartProduct();
        cartProduct.setId(product.getId());
        cartProduct.setCategory(product.getCategory());
        cartProduct.setName(product.getName());
        cartProduct.setPrice(product.getPrice());
        cartProduct.setDescription(product.getDescription());
        cartProduct.setQuantity(purchaseProduct.getQuantity());
        cartProducts.add(cartProduct);
      }
      if (!CollectionUtils.isEmpty(cartProducts)) {
        double totalSum = cartProducts.stream().mapToDouble(cartProduct -> cartProduct.getQuantity() * cartProduct.getPrice()).sum();
        if (searchParams.getMaxTotal() == null || totalSum <= searchParams.getMaxTotal()) {
          purchaseWithProducts.setCartProducts(cartProducts);
          result.add(purchaseWithProducts);
        }
      }
    }
    return result;
  }

}
