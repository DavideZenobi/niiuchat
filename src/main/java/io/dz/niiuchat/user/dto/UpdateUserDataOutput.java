package io.dz.niiuchat.user.dto;

import io.dz.niiuchat.domain.tables.pojos.Users;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserDataOutput {

  private Long id;
  private String username;
  private String email;

  public static UpdateUserDataOutput fromUser(Users input) {
    return UpdateUserDataOutput.builder()
        .id(input.getId())
        .username(input.getUsername())
        .email(input.getEmail())
        .build();
  }

}
