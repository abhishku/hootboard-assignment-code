package com.hootboard.assignment.controller;

import com.hootboard.assignment.entity.EmailAddress;
import com.hootboard.assignment.entity.PostalAddress;
import com.hootboard.assignment.entity.Users;
import com.hootboard.assignment.exception.ReadFailedException;
import com.hootboard.assignment.exception.UpdateFailedException;
import com.hootboard.assignment.repository.UserRepository;
import com.hootboard.assignment.request.CreateUserRequest;
import com.hootboard.assignment.response.ReadUser;
import com.hootboard.assignment.response.Status;
import com.hootboard.assignment.service.AuthorizationManager;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.naming.AuthenticationException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @Autowired
  UserRepository userRepository;
  @Autowired
  AuthorizationManager authorizationManager;

  @PostMapping("/user/create")
  public Status registerUser(@Valid @RequestBody CreateUserRequest newUserRequest) {
    System.out.println("Create user Request received: " + newUserRequest.toString());
    try {
      if (userRepository.findByUsername(newUserRequest.getUsername()) != null) {
        return Status.USER_ALREADY_EXISTS;
      } else {
        Users user = new Users(
            newUserRequest.getUsername(),
            newUserRequest.getPassword(),
            newUserRequest.getFirstName(),
            newUserRequest.getLastName());
        Set<EmailAddress> emailAddresses = newUserRequest.getEmailAddress().stream()
            .map(EmailAddress::new).collect(Collectors.toSet());
        user.getEmailAddresses().addAll(emailAddresses);
        Set<PostalAddress> postalAddress = newUserRequest.getPostalAddress().stream()
            .map(PostalAddress::new)
            .collect(Collectors.toSet());
        user.setEmailAddresses(emailAddresses);
        user.setPostalAddresses(postalAddress);
        userRepository.save(user);
        return Status.SUCCESS;
      }
    } catch (Exception ex) {
      return Status.USER_ALREADY_EXISTS;
    }
  }

  @GetMapping("/user/read")
  public ReadUser readUser(@RequestHeader("hoot-token") String hootToken)
      throws AuthenticationException {
    Long userId = checkIfAuthenticated(hootToken);
    Optional<Users> use = userRepository.findById(userId);
    if (use.isPresent()) {
      Users user = use.get();
      return ReadUser.builder().username(user.getUsername())
          .emailAddress(user.getEmailAddresses().stream().map(EmailAddress::getEmailAddress)
              .map(String::valueOf)
              .toArray(String[]::new))
          .firstName(user.getFirstName())
          .lastName(user.getLastName()).password(user.getPassword())
          .postalAddress(user.getPostalAddresses().stream().map(PostalAddress::getPostalAddresses)
              .map(String::valueOf)
              .toArray(String[]::new))
          .build();
    }
    throw new ReadFailedException();
  }

  @PatchMapping("/user/update")
  public void updateUser(@RequestHeader("hoot-token") String hootToken,
      @RequestBody CreateUserRequest updateUserRequest)
      throws AuthenticationException {
    Long userId = checkIfAuthenticated(hootToken);
    Optional<Users> us = userRepository.findById(userId);
    if (us.isPresent()) {
      Users user = us.get();
      System.out.println("Update Request Received " + updateUserRequest.toString());
      if (updateUserRequest.getUsername() != null) {
        user.setUsername(updateUserRequest.getUsername());
      }
      if (updateUserRequest.getPassword() != null) {
        user.setPassword(updateUserRequest.getPassword());
      }
      if (updateUserRequest.getFirstName() != null) {
        user.setFirstName(updateUserRequest.getFirstName());
      }
      if (updateUserRequest.getLastName() != null) {
        user.setLastName(updateUserRequest.getLastName());
      }
      if (updateUserRequest.getEmailAddress() != null) {
        user.getEmailAddresses().clear();
        user.getEmailAddresses().addAll(
            updateUserRequest.getEmailAddress().stream().map(EmailAddress::new).collect(
                Collectors.toSet()));
      }
      if (updateUserRequest.getPostalAddress() != null) {
        user.getPostalAddresses().clear();
        user.getPostalAddresses().addAll(
            updateUserRequest.getPostalAddress().stream().map(PostalAddress::new).collect(
                Collectors.toSet()));
      }
      userRepository.save(user);
      return;
    }
    throw new UpdateFailedException();
  }

  @DeleteMapping("/user/delete")
  public HttpStatus deleteUser(@RequestHeader("hoot-token") String hootToken) {
    try {
      Long userId = checkIfAuthenticated(hootToken);
      userRepository.deleteById(userId);
      return HttpStatus.OK;
    } catch (Exception ex) {
      return HttpStatus.NOT_FOUND;
    }
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(value = AuthenticationException.class)
  public Users handleAuthorizationException() {
    return null;
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(value = UpdateFailedException.class)
  public Users handleUpdateException() {
    return null;
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(value = ReadFailedException.class)
  public Users handleReadException() {
    return null;
  }

  private Long checkIfAuthenticated(String hootToken) throws AuthenticationException {
    Long userId = authorizationManager.getUserId(hootToken);
    if (userId == null) {
      throw new AuthenticationException("Please login first");
    }
    return userId;
  }

}
