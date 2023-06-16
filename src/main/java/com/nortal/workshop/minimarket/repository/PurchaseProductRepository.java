package com.nortal.workshop.minimarket.repository;

import com.nortal.workshop.minimarket.model.PurchaseProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PurchaseProductRepository extends JpaRepository<PurchaseProduct, Long> {

  List<PurchaseProduct> findByPurchaseId(Long purchaseId);

  List<PurchaseProduct> findByPurchaseIdAndQuantityLessThanEqual(Long purchaseId, Integer maxQuantity);

  @Query("select p from PurchaseProduct p where p.purchase.id = :purchaseId and (:maxQuantity is null or p.quantity <= :maxQuantity)")
  List<PurchaseProduct> findByPurchaseIdAndMaxQuantity(@Param("purchaseId") Long purchaseId, @Param("maxQuantity") Integer maxQuantity);

  @Query("select p from PurchaseProduct p where p.purchase.id in :ids")
  List<PurchaseProduct> findAllByPurchaseIds(@Param("ids") List<Long> purchaseIds);
}
