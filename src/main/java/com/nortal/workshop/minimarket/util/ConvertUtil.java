package com.nortal.workshop.minimarket.util;

import com.nortal.workshop.minimarket.model.Product;
import com.nortal.workshop.minimarket.model.Purchase;
import com.nortal.workshop.minimarket.model.PurchaseProduct;
import com.nortal.workshop.minimarket.model.rest.CartProductDTO;
import com.nortal.workshop.minimarket.model.rest.PurchaseDTO;

import java.util.Date;

public final class ConvertUtil {

  private ConvertUtil() {
    // private constructor
  }

  public static Purchase convertToPurchase(PurchaseDTO purchaseDTO) {
    Purchase purchase = new Purchase();
    purchase.setDate(new Date());
    purchase.setFirstName(purchaseDTO.getFirstName());
    purchase.setLastName(purchaseDTO.getLastName());
    purchase.setEmail(purchaseDTO.getEmail());
    return purchase;
  }

  public static PurchaseProduct convertToPurchaseProduct(Purchase purchase, CartProductDTO cartProduct) {
    PurchaseProduct purchaseProduct = new PurchaseProduct();
    Product product = convertToProduct(cartProduct);
    purchaseProduct.setProduct(product);
    purchaseProduct.setQuantity(cartProduct.getQuantity());
    purchaseProduct.setPurchase(purchase);
    return purchaseProduct;
  }

  public static Product convertToProduct(CartProductDTO cartProduct) {
    Product product = new Product();
    product.setId(cartProduct.getId());
    product.setName(cartProduct.getName());
    product.setCategory(cartProduct.getCategory());
    product.setPrice(cartProduct.getPrice());
    product.setDescription(cartProduct.getDescription());
    return product;
  }

  public static CartProductDTO convertToCartProductDTO(Product product, Integer quantity) {
    CartProductDTO cartProduct = new CartProductDTO();
    cartProduct.setId(product.getId());
    cartProduct.setCategory(product.getCategory());
    cartProduct.setName(product.getName());
    cartProduct.setPrice(product.getPrice());
    cartProduct.setDescription(product.getDescription());
    cartProduct.setQuantity(quantity);
    return cartProduct;
  }

  public static PurchaseDTO convertToPurchaseDTO(Purchase purchase) {
    PurchaseDTO purchaseDTO = new PurchaseDTO();
    purchaseDTO.setId(purchase.getId());
    purchaseDTO.setFirstName(purchase.getFirstName());
    purchaseDTO.setLastName(purchase.getLastName());
    purchaseDTO.setEmail(purchase.getEmail());
    purchaseDTO.setDate(purchase.getDate());
    return purchaseDTO;
  }
}
