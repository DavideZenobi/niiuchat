package io.dz.niiuchat.messaging.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateGroupOutput {

  private String groupId;

}
