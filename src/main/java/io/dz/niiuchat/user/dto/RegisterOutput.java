package io.dz.niiuchat.user.dto;

import io.dz.niiuchat.domain.tables.pojos.Users;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterOutput {

  private Long id;
  private String username;
  private String email;
  private String status;

  public static RegisterOutput fromUser(Users input) {
    return RegisterOutput.builder()
        .id(input.getId())
        .username(input.getUsername())
        .email(input.getEmail())
        .status(input.getStatus())
        .build();
  }

}
