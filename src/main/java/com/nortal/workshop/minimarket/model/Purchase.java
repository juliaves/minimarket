package com.nortal.workshop.minimarket.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "purchase")
@AllArgsConstructor
@NoArgsConstructor
public class Purchase implements Serializable {
  public Long getId() {
    return id;
  }
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  public String getEmail() {
    return email;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public String getFirstName() {
    return firstName;
  }
  @Column(name = "date", nullable = false)
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
  private Date date;
  public String getLastName() {
    return lastName;
  }
  @Column(name = "last_name", nullable = false)
  private String lastName;
  @Column(name = "emali")
  private String email;
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public Date getDate() {
    return date;
  }
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id", updatable = false, nullable = false)
  private Long id;
  public void setDate(Date date) {
    this.date = date;
  }
  @Column(name = "first_name", nullable = false)
  private String firstName;
}
