package io.dz.niiuchat.messaging.dto;

import lombok.Data;

@Data
public class MessageInput {

  private String groupId;
  private String message;
  private Boolean hasAttachment;



}
