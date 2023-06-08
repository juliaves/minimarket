package com.nortal.workshop.minimarket.model;

import com.nortal.workshop.minimarket.enums.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(updatable = false, nullable = false)
  private Long id;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private Double price;
  @Column(nullable = false)
  private String category;
  @Column(length = 35)
  private String description;

  public boolean validate(){
    ProductCategory.valueOf(this.getCategory());
    return StringUtils.isNotEmpty(this.getName())
        && ObjectUtils.isNotEmpty(this.getPrice())
        && this.getPrice().compareTo(0d) > 0
        && ObjectUtils.isNotEmpty(this.getId())
        && this.getId().compareTo(0L) > 0
        && this.getDescription().length() <= 35;
  }

}
