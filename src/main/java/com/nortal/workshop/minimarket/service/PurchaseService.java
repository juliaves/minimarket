package com.nortal.workshop.minimarket.service;

import com.nortal.workshop.minimarket.model.rest.PurchaseDTO;
import com.nortal.workshop.minimarket.model.rest.PurchaseFilterParams;

import java.util.List;

public interface PurchaseService {

  void save(PurchaseDTO purchaseDTO);

  List<PurchaseDTO> getAll();

  List<PurchaseDTO> searchPurchases(PurchaseFilterParams searchParams);

}
