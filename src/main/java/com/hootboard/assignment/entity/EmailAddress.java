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

@Getter
@Entity
@Table(name = "emailAddress")
public class EmailAddress implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;


  private String emailAddress;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private Users users;

  public EmailAddress(Long id, String emailAddress) {
    this.id = id;
    this.emailAddress = emailAddress;
  }

  public EmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public EmailAddress() {
  }
}