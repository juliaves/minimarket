package com.nortal.workshop.minimarket.repository;

import com.nortal.workshop.minimarket.model.Product;
import com.nortal.workshop.minimarket.model.PurchaseProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

  List<Product> findByPriceIsLessThanEqual(Double maxPrice);

  List<Product> findByCategoryAndPriceIsLessThanEqual(String category, Double maxPrice);

  @Query("select p from PurchaseProduct p where p.purchase.id = :purchaseId and (:maxQuantity is null or p.quantity <= :maxQuantity)")
  List<PurchaseProduct> findByPurchaseIdAndMaxQuantity(@Param("purchaseId") Long purchaseId, @Param("maxQuantity") Integer maxQuantity);

}
