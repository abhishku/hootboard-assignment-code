package com.hootboard.assignment.controller;

import com.hootboard.assignment.service.AuthorizationManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AuthController {

  @Autowired
  AuthorizationManager authorizationManager;

  @PostMapping("/user/logout")
  public HttpStatus logoutUser(@RequestHeader("Authorization") String authHeader) {
    log.trace("Logout request received: " + authHeader);
    if (authorizationManager.logOut(authHeader)) {
      return HttpStatus.OK;
    } else {
      return HttpStatus.NOT_FOUND;
    }
  }

  @PostMapping("/user/login")
  public String loginUser(@RequestHeader("Authorization") String authHeader) {
    try {
      return authorizationManager.checkAuthorization(authHeader);
    } catch (Exception e) {
      return HttpStatus.UNAUTHORIZED.toString();
    }
  }

}
