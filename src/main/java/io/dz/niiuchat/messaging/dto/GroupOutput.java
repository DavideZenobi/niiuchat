package io.dz.niiuchat.messaging.dto;

import io.dz.niiuchat.domain.tables.pojos.Chats;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupOutput {

  private String groupId;
  private Long userId;
  private String username;

  public static GroupOutput fromGroup(Object input) {
    return GroupOutput.builder()
        .build();
  }
}
