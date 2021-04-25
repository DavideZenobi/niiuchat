package io.dz.niiuchat.user.dto;

import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserPasswordInput {

  @Size(min = 4, max = 20)
  private String password;

}
