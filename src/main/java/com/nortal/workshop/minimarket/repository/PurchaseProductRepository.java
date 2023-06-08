package com.nortal.workshop.minimarket.repository;

import com.nortal.workshop.minimarket.model.PurchaseProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseProductRepository extends JpaRepository<PurchaseProduct, Long> {

  List<PurchaseProduct> findByPurchaseId(Long purchaseId);

  List<PurchaseProduct> findByPurchaseIdAndQuantityLessThanEqual(Long purchaseId, Integer maxQuantity);
}
