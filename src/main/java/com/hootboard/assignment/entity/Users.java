package com.hootboard.assignment.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Entity
@Table(name = "users")
@Getter
public class Users implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private @NotBlank String username;
  private @NotBlank String password;
  private String firstName;
  private String lastName;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  Set<EmailAddress> emailAddresses = new HashSet<>();

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  Set<PostalAddress> postalAddresses = new HashSet<>();

  public Users(@NotBlank String username,
      @NotBlank String password, String firstName, String lastName) {
    this.username = username;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public Users() {
  }

  public void setEmailAddresses(
      Set<EmailAddress> emailAddresses) {
    this.emailAddresses = emailAddresses;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setPostalAddresses(
      Set<PostalAddress> postalAddresses) {
    this.postalAddresses = postalAddresses;
  }

  public Set<EmailAddress> getEmailAddresses() {
    return emailAddresses;
  }

  public Set<PostalAddress> getPostalAddresses() {
    return postalAddresses;
  }
}