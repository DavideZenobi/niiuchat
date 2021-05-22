package io.dz.niiuchat.websocket.dto.content;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MessageReceivedMessage implements LiveMessageContent {

    private Long userId;
    private String groupId;
    private String message;
    private Boolean hasAttachment;
    private LocalDateTime date;

}
