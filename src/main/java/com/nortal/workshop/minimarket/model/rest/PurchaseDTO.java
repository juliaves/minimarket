package com.nortal.workshop.minimarket.model.rest;

import com.nortal.workshop.minimarket.model.Purchase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PurchaseDTO extends Purchase {
  private List<CartProductDTO> cartProducts;

  public boolean validate(){
    return StringUtils.isNotEmpty(this.getFirstName())
        && StringUtils.isNotEmpty(this.getLastName())
        && !CollectionUtils.isEmpty(this.getCartProducts());
  }
}
