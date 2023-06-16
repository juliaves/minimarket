package com.nortal.workshop.minimarket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="purchase_product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseProduct implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name="id", updatable = false, nullable = false)
  private Long id;
  @ManyToOne
  @JoinColumn(name="purchase_id", nullable=false)
  private Purchase purchase;
  @ManyToOne
  @JoinColumn(name="product_id", nullable=false)
  private Product product;
  @Column(name="quantity", nullable = false)
  private Integer quantity;
}
