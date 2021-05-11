package io.dz.niiuchat.websocket.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserConnectedMessage implements LiveMessageContent {

    private Long userId;
    private LocalDateTime date;

}
