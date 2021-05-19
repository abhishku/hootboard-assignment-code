package com.hootboard.assignment.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@Table(name = "PostalAddress")
public class PostalAddress implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;


  private String postalAddresses;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private Users users;

  public PostalAddress(String postalAddresses) {
    this.postalAddresses = postalAddresses;
  }

  public PostalAddress() {
  }

}