package com.hootboard.assignment.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.hootboard.assignment.entity.Users;
import com.hootboard.assignment.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.naming.AuthenticationException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationManager {

  Cache<Long, String> authCache = Caffeine.newBuilder()
      .expireAfterWrite(1, TimeUnit.MINUTES)
      .maximumSize(1000)
      .build();
  Cache<String, Long> tokenCache = Caffeine.newBuilder()
      .expireAfterWrite(1, TimeUnit.MINUTES)
      .maximumSize(1000)
      .build();

  private final UserRepository userRepository;

  public AuthorizationManager(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public String checkAuthorization(String authHeader) throws Exception {
    if (authHeader != null && authHeader.startsWith("Basic")) {
      Users resultFromDB = getFromDb(authHeader);
      if (resultFromDB != null && authCache.getIfPresent(resultFromDB.getId()) != null) {
        return authCache.getIfPresent(resultFromDB.getId());
      } else if (resultFromDB != null) {
        String authToken = UUID.randomUUID().toString();
        authCache.put(resultFromDB.getId(), authToken);
        tokenCache.put(authToken, resultFromDB.getId());
        return authToken;
      }
    }
    throw new AuthenticationException("The authorization header is either empty or isn't Basic.");
  }

  public Boolean logOut(String authHeader) {
    if (authHeader != null && authHeader.startsWith("Basic")) {
      Users resultFromDB = getFromDb(authHeader);
      if (resultFromDB != null) {
        System.out.println("Removing this from cache: " + resultFromDB.getUsername());
        System.out.println("Removing Token cache: " + authCache.getIfPresent(resultFromDB.getId()));
        if (authCache.getIfPresent(resultFromDB.getId()) != null) {
          tokenCache.invalidate(authCache.getIfPresent(resultFromDB.getId()));
          authCache.invalidate(resultFromDB.getId());
        }
        return true;
      }
    }
    return false;
  }

  public Long getUserId(String hootToken) {
    return tokenCache.getIfPresent(hootToken);
  }

  private Users getFromDb(String authHeader) {
    String encodedUsernamePassword = authHeader.substring("Basic ".length()).trim();
    String usernamePassword = new String(Base64.decodeBase64(encodedUsernamePassword),
        StandardCharsets.UTF_8);
    int separatorIndex = usernamePassword.indexOf(':');
    String username = usernamePassword
        .substring(0, separatorIndex);
    String password = usernamePassword.substring(separatorIndex + 1);
    return checkIfUserExists(username, password);
  }

  private Users checkIfUserExists(String username, String password) {
    return userRepository.findByUsernameAndPassword(username, password);
  }

}
