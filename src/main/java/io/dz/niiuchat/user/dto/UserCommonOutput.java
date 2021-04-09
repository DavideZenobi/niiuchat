package io.dz.niiuchat.user.dto;

import io.dz.niiuchat.domain.tables.pojos.Users;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCommonOutput {

  private final Long id;
  private final String username;

  public static UserCommonOutput from(Users user) {
    return new UserCommonOutput(user.getId(), user.getUsername());
  }

}
