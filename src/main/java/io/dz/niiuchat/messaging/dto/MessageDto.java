package io.dz.niiuchat.messaging.dto;

import io.dz.niiuchat.domain.tables.pojos.Attachments;
import io.dz.niiuchat.domain.tables.pojos.Messages;
import lombok.Data;

@Data
public class MessageDto {

  private final Messages message;
  private final Attachments attachment;

}
