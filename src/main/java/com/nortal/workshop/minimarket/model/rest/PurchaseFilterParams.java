package com.nortal.workshop.minimarket.model.rest;

import lombok.Data;

import java.io.Serializable;

@Data
public class PurchaseFilterParams implements Serializable {
  private String firstName;
  private String lastName;
  private String productName;
  private Double maxPrice;
  private Double maxTotal;
  private Integer maxQuantity;
}
