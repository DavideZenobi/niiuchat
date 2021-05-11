package io.dz.niiuchat.websocket.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
public class LiveMessage {

    private ContentType type;
    private LiveMessageContent content;

    public static LiveMessage createUserConnectedMessage(Long userId) {
        final var liveMessage = new LiveMessage();
        liveMessage.setType(ContentType.USER_CONNECTED);

        final UserConnectedMessage userConnectedMessage = new UserConnectedMessage();
        userConnectedMessage.setUserId(userId);
        userConnectedMessage.setDate(LocalDateTime.now(ZoneOffset.UTC));

        liveMessage.setContent(userConnectedMessage);

        return liveMessage;
    }

}
