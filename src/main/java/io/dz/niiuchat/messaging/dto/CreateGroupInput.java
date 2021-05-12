package io.dz.niiuchat.messaging.dto;

import java.util.Set;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateGroupInput {

  @NotEmpty
  private Set<Long> userIds;

}
