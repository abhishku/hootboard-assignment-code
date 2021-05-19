package com.hootboard.assignment.service;

import com.hootboard.assignment.entity.Users;
import com.hootboard.assignment.repository.UserRepository;
import javax.naming.AuthenticationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class AuthorizationManagerTest {

  @Mock
  UserRepository userRepository;
  AuthorizationManager authorizationManager;
  String authorizationToken;
  ArgumentCaptor<String> captureString = ArgumentCaptor.forClass(String.class);

  @BeforeEach
  void setUp() {
    authorizationManager = new AuthorizationManager(userRepository);
    authorizationToken = "Basic YWJoaXNoZWs6cGFzc3dvcmQ=";
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void checkAuthorizationThrowsWhenuserNotExist() {
    Assertions.assertThrows(AuthenticationException.class, () -> {
          authorizationManager.checkAuthorization(authorizationToken);
        }
    );
  }

  @Test
  void checkAuthorizationThrowsWhenEmptyorNotBasicExist() {
    Assertions.assertThrows(AuthenticationException.class, () -> {
      Users a = new Users("abhishek", "password", "Abhishek", "Kumar");
      a.setId(2L);
      BDDMockito.when(userRepository.findByUsernameAndPassword("abhishek", "password"))
          .thenReturn(a);
      BDDMockito.when(authorizationManager.checkAuthorization(Mockito.anyString())).thenReturn(
          captureString.capture());
    });
  }

  @Test
  void checkAuthorizationCheckifAllsGood() throws Exception {
    Users a = new Users("abhishek", "password", "Abhishek", "Kumar");
    a.setId(2L);
    BDDMockito.when(userRepository.findByUsernameAndPassword("abhishek", "password"))
        .thenReturn(a);
    String answer = authorizationManager.checkAuthorization(authorizationToken);
  }


}