package com.nortal.workshop.minimarket.model.rest;

import com.nortal.workshop.minimarket.model.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartProductDTO extends Product {
  private Integer quantity;
}
