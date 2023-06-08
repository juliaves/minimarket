package com.nortal.workshop.minimarket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseWithProducts extends Purchase {
  private List<CartProduct> cartProducts;

  public boolean validate(){
    return StringUtils.isNotEmpty(this.getFirstName())
        && StringUtils.isNotEmpty(this.getLastName())
        && !CollectionUtils.isEmpty(this.getCartProducts());
  }
}
