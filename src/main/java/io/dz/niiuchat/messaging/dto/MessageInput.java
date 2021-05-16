package io.dz.niiuchat.messaging.dto;

import lombok.Data;

@Data
public class MessageInput {

  private String groupId;
  private String message;
  private Boolean hasAttachment;

  public byte hasAttachmentAsByte() {
    return (byte) (Boolean.TRUE.equals(hasAttachment) ? 1 : 0);
  }

}
