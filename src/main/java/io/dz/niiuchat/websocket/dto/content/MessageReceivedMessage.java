package io.dz.niiuchat.websocket.dto.content;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageReceivedMessage implements LiveMessageContent {

    private Long userId;
    private String groupId;
    private String message;
    private LocalDateTime date;

}
