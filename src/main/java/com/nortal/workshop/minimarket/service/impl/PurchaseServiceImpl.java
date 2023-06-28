package com.nortal.workshop.minimarket.service.impl;

import com.nortal.workshop.minimarket.model.Product;
import com.nortal.workshop.minimarket.model.Purchase;
import com.nortal.workshop.minimarket.model.PurchaseProduct;
import com.nortal.workshop.minimarket.model.rest.CartProductDTO;
import com.nortal.workshop.minimarket.model.rest.PurchaseDTO;
import com.nortal.workshop.minimarket.model.rest.PurchaseFilterParams;
import com.nortal.workshop.minimarket.repository.PurchaseProductRepository;
import com.nortal.workshop.minimarket.repository.PurchaseRepository;
import com.nortal.workshop.minimarket.service.PurchaseService;
import com.nortal.workshop.minimarket.util.ConvertUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
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
    final Purchase purchase = purchaseRepository.save(ConvertUtil.convertToPurchase(purchaseDTO));
    List<PurchaseProduct> purchaseProducts = purchaseDTO.getCartProducts()
        .stream()
        .map(cartProduct -> ConvertUtil.convertToPurchaseProduct(purchase, cartProduct))
        .collect(Collectors.toList());
    purchaseProductRepository.saveAll(purchaseProducts);
  }

  @Override
  public List<PurchaseDTO> getAll() {
    List<Purchase> allPurchases = purchaseRepository.findAll();
    if (CollectionUtils.isEmpty(allPurchases)) {
      return Collections.emptyList();
    }
    List<PurchaseDTO> purchasesWithProducts = new ArrayList<>();

    Map<Long, List<PurchaseProduct>> purchaseProductsMap = findAllPurchaseProducts(getAllPurchasesIds(allPurchases));
    allPurchases.forEach(purchase -> {
      PurchaseDTO purchaseDTO = ConvertUtil.convertToPurchaseDTO(purchase);
      List<PurchaseProduct> purchaseProducts = purchaseProductsMap.get(purchase.getId());
      List<CartProductDTO> cartProducts = new ArrayList<>();
      purchaseProducts.forEach(purchaseProduct -> cartProducts.add(ConvertUtil.convertToCartProductDTO(purchaseProduct.getProduct(),
                                                                                           purchaseProduct.getQuantity())));
      purchaseDTO.setCartProducts(cartProducts);
      purchasesWithProducts.add(purchaseDTO);
    });
    return purchasesWithProducts;
  }

  private List<Long> getAllPurchasesIds(List<Purchase> allPurchases) {
    return allPurchases.stream().map(Purchase::getId).collect(Collectors.toList());
  }

  @Override
  public List<PurchaseDTO> searchPurchases(PurchaseFilterParams searchParams) {
    List<Purchase> purchases = findPurchasesByFirstAndLastName(searchParams.getFirstName(), searchParams.getLastName());
    if (CollectionUtils.isEmpty(purchases)) {
      return Collections.emptyList();
    }
    List<PurchaseDTO> result = new ArrayList<>();
    for (Purchase purchase : purchases) {
      List<PurchaseProduct> purchaseProducts = getPurchaseProducts(searchParams, purchase);
      if (CollectionUtils.isEmpty(purchaseProducts)) {
        continue;
      }
      List<CartProductDTO> cartProducts = getCartProductDTOs(searchParams, purchaseProducts);
      if (CollectionUtils.isEmpty(cartProducts)) {
        continue;
      }
      double totalSum = getCartProductsTotalSum(cartProducts);
      if (searchParams.getMaxTotal() == null || totalSum <= searchParams.getMaxTotal()) {
        PurchaseDTO purchaseDTO = ConvertUtil.convertToPurchaseDTO(purchase);
        purchaseDTO.setCartProducts(cartProducts);
        result.add(purchaseDTO);
      }
    }
    return result;
  }

  private static double getCartProductsTotalSum(List<CartProductDTO> cartProducts) {
    return cartProducts.stream().mapToDouble(cartProduct -> cartProduct.getQuantity() * cartProduct.getPrice()).sum();
  }

  private static List<CartProductDTO> getCartProductDTOs(PurchaseFilterParams searchParams, List<PurchaseProduct> purchaseProducts) {
    List<CartProductDTO> cartProducts = new ArrayList<>();
    for (PurchaseProduct purchaseProduct : purchaseProducts) {
      Product product = isSuitableProduct(searchParams, purchaseProduct) ? purchaseProduct.getProduct() : null;
      if (product == null) {
        continue;
      }
      cartProducts.add(ConvertUtil.convertToCartProductDTO(product, purchaseProduct.getQuantity()));
    }
    return cartProducts;
  }

  private static boolean isSuitableProduct(PurchaseFilterParams searchParams, PurchaseProduct purchaseProduct) {
    String trimmedProductName = getTrimmedStringValue(searchParams.getProductName());
    return (StringUtils.isEmpty(trimmedProductName) || purchaseProduct.getProduct()
        .getName()
        .toLowerCase()
        .contains(trimmedProductName.toLowerCase()))
        && (searchParams.getMaxPrice() == null
        || ObjectUtils.compare(purchaseProduct.getProduct().getPrice(), searchParams.getMaxPrice()) <= 0);
  }

  private List<PurchaseProduct> getPurchaseProducts(PurchaseFilterParams searchParams, Purchase purchase) {
    if (searchParams.getMaxQuantity() == null) {
      return purchaseProductRepository.findByPurchaseId(purchase.getId());
    }
    return purchaseProductRepository.findByPurchaseIdAndQuantityLessThanEqual(
        purchase.getId(),
        searchParams.getMaxQuantity());
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
