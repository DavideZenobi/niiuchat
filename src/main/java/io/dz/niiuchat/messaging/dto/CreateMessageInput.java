package io.dz.niiuchat.messaging.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateMessageInput {

  @NotBlank
  private String groupId;

  private String message;

  private MultipartFile attachment;

  public Boolean hasAttachment() {
    return attachment != null;
  }

  public byte hasAttachmentAsByte() {
    return (byte) (attachment != null ? 1 : 0);
  }

}
