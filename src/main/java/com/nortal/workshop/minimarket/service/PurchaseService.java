package com.nortal.workshop.minimarket.service;

import com.nortal.workshop.minimarket.model.Purchase;
import com.nortal.workshop.minimarket.model.PurchaseFilterParams;
import com.nortal.workshop.minimarket.model.PurchaseWithProducts;

import java.util.List;

public interface PurchaseService {

  void save(PurchaseWithProducts purchaseWithProducts);

  List<PurchaseWithProducts> getAll();

  List<PurchaseWithProducts> searchPurchases(PurchaseFilterParams searchParams);

}
