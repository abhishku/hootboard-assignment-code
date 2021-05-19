package com.hootboard.assignment.response;

import com.hootboard.assignment.entity.EmailAddress;
import com.hootboard.assignment.entity.PostalAddress;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReadUser implements Serializable {

  private @NotBlank String username;
  private @NotBlank String password;
  private String firstName;
  private String lastName;
  private String[] emailAddress;
  private String[] postalAddress;

}
