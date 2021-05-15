package io.dz.niiuchat.websocket.dto;

import io.dz.niiuchat.websocket.dto.content.LiveMessageContent;
import lombok.Data;

@Data
public class LiveMessage {

    private ContentType type;
    private LiveMessageContent content;

    LiveMessage() { }

}
