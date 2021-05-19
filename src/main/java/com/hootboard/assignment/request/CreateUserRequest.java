package com.hootboard.assignment.request;

import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class CreateUserRequest {
  private @NotBlank String username;
  private @NotBlank String password;
  private String firstName;
  private String lastName;
  private List<String> emailAddress;
  private List<String> postalAddress;
}
