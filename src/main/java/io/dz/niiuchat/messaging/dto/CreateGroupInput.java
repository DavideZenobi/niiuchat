package io.dz.niiuchat.messaging.dto;

import java.util.Set;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateGroupInput {

  @Size(min = 1)
  private Set<Long> userIds;

}
