package io.dz.niiuchat.websocket.dto.content;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class GroupCreatedMessage implements LiveMessageContent {

    private String groupId;
    private LocalDateTime date;

}
