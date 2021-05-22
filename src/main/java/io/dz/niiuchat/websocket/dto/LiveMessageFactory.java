package io.dz.niiuchat.websocket.dto;

import io.dz.niiuchat.websocket.dto.content.GroupCreatedMessage;
import io.dz.niiuchat.websocket.dto.content.GroupDeletedMessage;
import io.dz.niiuchat.websocket.dto.content.MessageReceivedMessage;
import io.dz.niiuchat.websocket.dto.content.UserConnectedMessage;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class LiveMessageFactory {

    private LiveMessageFactory() { }

    public static LiveMessage createUserConnectedMessage(Long userId) {
        var liveMessage = new LiveMessage();
        liveMessage.setType(ContentType.USER_CONNECTED);

        var userConnectedMessage = new UserConnectedMessage();
        userConnectedMessage.setUserId(userId);
        userConnectedMessage.setDate(LocalDateTime.now(ZoneOffset.UTC));

        liveMessage.setContent(userConnectedMessage);

        return liveMessage;
    }

    public static LiveMessage createMessageReceivedMessage(Long userId, String groupId, String message) {
        var liveMessage = new LiveMessage();
        liveMessage.setType(ContentType.MESSAGE_RECEIVED);

        var messageReceivedMessage = new MessageReceivedMessage();
        messageReceivedMessage.setUserId(userId);
        messageReceivedMessage.setGroupId(groupId);
        messageReceivedMessage.setMessage(message);
        messageReceivedMessage.setDate(LocalDateTime.now(ZoneOffset.UTC));

        liveMessage.setContent(messageReceivedMessage);

        return liveMessage;
    }

    public static LiveMessage createGroupCreatedMessage(String groupId) {
        var liveMessage = new LiveMessage();
        liveMessage.setType(ContentType.GROUP_CREATED);

        var groupCreatedMessage = new GroupCreatedMessage();
        groupCreatedMessage.setGroupId(groupId);
        groupCreatedMessage.setDate(LocalDateTime.now(ZoneOffset.UTC));

        liveMessage.setContent(groupCreatedMessage);

        return liveMessage;
    }

    public static LiveMessage createGroupDeletedMessage(String groupId) {
        var liveMessage = new LiveMessage();
        liveMessage.setType(ContentType.GROUP_DELETED);

        var groupDeletedMessage = new GroupDeletedMessage();
        groupDeletedMessage.setGroupId(groupId);
        groupDeletedMessage.setDate(LocalDateTime.now(ZoneOffset.UTC));

        liveMessage.setContent(groupDeletedMessage);

        return liveMessage;
    }

}
