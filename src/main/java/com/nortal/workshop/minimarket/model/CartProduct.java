package com.nortal.workshop.minimarket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartProduct extends Product {
  private Integer quantity;
}
