package com.nortal.workshop.minimarket.rest;

import com.nortal.workshop.minimarket.model.rest.PurchaseFilterParams;
import com.nortal.workshop.minimarket.model.rest.PurchaseDTO;
import com.nortal.workshop.minimarket.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/purchases")
public class PurchaseResource {

  @Autowired
  private PurchaseService purchaseService;

  @PostMapping(consumes = "application/json")
  public ResponseEntity<String> save(@NonNull @RequestBody PurchaseDTO purchase) {
    if (!purchase.validate()) {
      return ResponseEntity.badRequest().build();
    }
    purchaseService.save(purchase);
    return ResponseEntity.ok().body("Purchase is successfully saved!");
  }

  @GetMapping(produces = "application/json")
  public List<PurchaseDTO> getAllPurchases() {
    return purchaseService.getAll();
  }

  @PostMapping(path = "/search",consumes = "application/json")
  public List<PurchaseDTO> searchPurchases(@NonNull @RequestBody PurchaseFilterParams searchParams) {
    return purchaseService.searchPurchases(searchParams);
  }

}
