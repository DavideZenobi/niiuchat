package io.dz.niiuchat.user.register;

import io.dz.niiuchat.domain.tables.pojos.Users;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterInput {

  @NotBlank
  private String username;

  @Email
  private String email;

  @Size(min = 4, max = 16)
  private String password;

  public Users toUser() {
    Users result = new Users();
    result.setUsername(username);
    result.setEmail(email);
    result.setPassword(password);

    return result;
  }

}
