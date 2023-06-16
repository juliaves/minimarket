package com.nortal.workshop.minimarket.service.impl;

import com.nortal.workshop.minimarket.model.Product;
import com.nortal.workshop.minimarket.model.Purchase;
import com.nortal.workshop.minimarket.model.PurchaseProduct;
import com.nortal.workshop.minimarket.model.rest.CartProductDTO;
import com.nortal.workshop.minimarket.model.rest.PurchaseDTO;
import com.nortal.workshop.minimarket.repository.PurchaseProductRepository;
import com.nortal.workshop.minimarket.repository.PurchaseRepository;
import com.nortal.workshop.minimarket.service.PurchaseService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("purchaseService")
public class PurchaseServiceImpl implements PurchaseService {

  @Autowired
  private PurchaseRepository purchaseRepository;
  @Autowired
  private PurchaseProductRepository purchaseProductRepository;

  @Override
  public void save(PurchaseDTO purchaseDTO) {
    Purchase purchase = magic1(purchaseDTO);
    purchase = purchaseRepository.save(purchase);

    List<PurchaseProduct> purchaseProducts = new ArrayList<>();
    for (CartProductDTO cartProduct : purchaseDTO.getCartProducts()) {
      PurchaseProduct purchaseProduct = magic2(purchase, cartProduct);
      purchaseProducts.add(purchaseProduct);
    }
    purchaseProductRepository.saveAll(purchaseProducts);
  }


  private static Purchase magic1(PurchaseDTO purchaseDTO) {
    Purchase purchase = new Purchase();
    purchase.setDate(new Date());
    purchase.setFirstName(purchaseDTO.getFirstName());
    purchase.setLastName(purchaseDTO.getLastName());
    purchase.setEmail(purchaseDTO.getEmail());
    return purchase;
  }

  private static PurchaseProduct magic2(Purchase purchase, CartProductDTO cartProduct) {
    PurchaseProduct purchaseProduct = new PurchaseProduct();
    Product product = magic3(cartProduct);
    purchaseProduct.setProduct(product);
    purchaseProduct.setQuantity(cartProduct.getQuantity());
    purchaseProduct.setPurchase(purchase);
    return purchaseProduct;
  }

  private static Product magic3(CartProductDTO cartProduct) {
    Product product = new Product();
    product.setId(cartProduct.getId());
    product.setName(cartProduct.getName());
    product.setCategory(cartProduct.getCategory());
    product.setPrice(cartProduct.getPrice());
    product.setDescription(cartProduct.getDescription());
    return product;
  }

  @Override
  public List<PurchaseDTO> getAll() {
    List<Purchase> allPurchases = purchaseRepository.findAll();
    List<PurchaseDTO> purchasesWithProducts = new ArrayList<>();
    if (CollectionUtils.isEmpty(allPurchases)) {
      return purchasesWithProducts;
    }
    allPurchases.forEach(p -> {
      PurchaseDTO pp1 = new PurchaseDTO();
      pp1.setId(p.getId());
      pp1.setDate(p.getDate());
      pp1.setEmail(p.getEmail());
      pp1.setFirstName(p.getFirstName());
      pp1.setLastName(p.getLastName());

      List<CartProductDTO> cartProducts = new ArrayList<>();
      List<PurchaseProduct> purchaseProducts = purchaseProductRepository.findByPurchaseId(p.getId());
      purchaseProducts.forEach(pp -> {
        CartProductDTO cp = new CartProductDTO();
        Product p1 = pp.getProduct();
        cp.setId(p1.getId());
        cp.setCategory(p1.getCategory());
        cp.setName(p1.getName());
        cp.setPrice(p1.getPrice());
        cp.setDescription(p1.getDescription());
        cp.setQuantity(pp.getQuantity());
        cartProducts.add(cp);
      });
      pp1.setCartProducts(cartProducts);
      purchasesWithProducts.add(pp1);
    });
    return purchasesWithProducts;
  }

  @Override
  public List<PurchaseDTO> searchPurchases(String firstName,
                                           String lastName,
                                           String productName,
                                           Double maxPrice,
                                           Double maxTotal,
                                           Integer maxQuantity) {
    List<Purchase> purchases = findPurchasesByFirstAndLastName(firstName, lastName);
    if (CollectionUtils.isEmpty(purchases)) {
      return Collections.emptyList();
    }
    List<PurchaseDTO> result = new ArrayList<>();
    for (Purchase purchase : purchases) {
      List<PurchaseProduct> purchaseProducts;
      if (maxQuantity == null) {
        purchaseProducts = purchaseProductRepository.findByPurchaseId(purchase.getId());
      } else {
        purchaseProducts = purchaseProductRepository.findByPurchaseIdAndQuantityLessThanEqual(
            purchase.getId(),
            maxQuantity);
      }
      if (CollectionUtils.isEmpty(purchaseProducts)) {
        continue;
      }
      PurchaseDTO purchaseDTO = new PurchaseDTO();
      purchaseDTO.setId(purchase.getId());
      purchaseDTO.setFirstName(purchase.getFirstName());
      purchaseDTO.setLastName(purchase.getLastName());
      purchaseDTO.setEmail(purchase.getEmail());
      purchaseDTO.setDate(purchase.getDate());

      List<CartProductDTO> cartProducts = new ArrayList<>();
      for (PurchaseProduct purchaseProduct : purchaseProducts) {
        Product product = null;
        String trimmedProductName = getTrimmedStringValue(productName).toLowerCase();
        if ((StringUtils.isEmpty(trimmedProductName) || purchaseProduct.getProduct().getName().toLowerCase().contains(trimmedProductName))
            && (maxPrice == null
            || ObjectUtils.compare(purchaseProduct.getProduct().getPrice(), maxPrice) <= 0)) {
          product = purchaseProduct.getProduct();
        }
        if (product == null) {
          continue;
        }
        CartProductDTO cartProduct = new CartProductDTO();
        cartProduct.setId(product.getId());
        cartProduct.setCategory(product.getCategory());
        cartProduct.setName(product.getName());
        cartProduct.setPrice(product.getPrice());
        cartProduct.setDescription(product.getDescription());
        cartProduct.setQuantity(purchaseProduct.getQuantity());
        cartProducts.add(cartProduct);
      }
      if (!CollectionUtils.isEmpty(cartProducts)) {
        double totalSum =
            cartProducts.stream().mapToDouble(cartProduct -> cartProduct.getQuantity() * cartProduct.getPrice()).sum();
        if (maxTotal == null || totalSum <= maxTotal) {
          purchaseDTO.setCartProducts(cartProducts);
          result.add(purchaseDTO);
        }
      }
    }
    return result;
  }

  private List<Purchase> findPurchasesByFirstAndLastName(String firstName, String lastName) {
    return purchaseRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(
        getTrimmedStringValue(firstName),
        getTrimmedStringValue(lastName));
  }

  private static String getTrimmedStringValue(String param) {
    return StringUtils.defaultIfEmpty(param, "").trim();
  }

  /*THIS METHOD CAN BE HELPFUL FOR REFACTORING #getAll() METHOD*/
  private Map<Long, List<PurchaseProduct>> findAllPurchaseProducts(List<Long> purchaseIds) {
    return purchaseProductRepository.findAllByPurchaseIds(purchaseIds).stream()
        .collect(Collectors.groupingBy(
            purchaseProduct -> purchaseProduct.getPurchase().getId(),
            Collectors.mapping(
                Function.identity(),
                Collectors.toList())
        ));
  }

}
