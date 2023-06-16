package com.nortal.workshop.minimarket.model.rest;

import com.nortal.workshop.minimarket.enums.ProductCategory;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Data
public class ProductDTO implements Serializable {
  private Long id;
  private String name;
  private Double price;
  private String category;
  private String description;

  public boolean validate(){
    ProductCategory.valueOf(this.getCategory());
    return StringUtils.isNotEmpty(this.getName())
        && ObjectUtils.isNotEmpty(this.getPrice())
        && this.getPrice().compareTo(0d) > 0;
  }
}
