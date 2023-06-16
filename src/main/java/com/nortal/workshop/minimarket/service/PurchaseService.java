package com.nortal.workshop.minimarket.service;

import com.nortal.workshop.minimarket.model.rest.PurchaseDTO;

import java.util.List;

public interface PurchaseService {

  void save(PurchaseDTO purchaseDTO);

  List<PurchaseDTO> getAll();

  List<PurchaseDTO> searchPurchases(String firstName,
                                    String lastName,
                                    String productName,
                                    Double maxPrice,
                                    Double maxTotal,
                                    Integer maxQuantity);

}
