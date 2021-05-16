package io.dz.niiuchat.messaging;

import io.dz.niiuchat.domain.tables.pojos.Chats;
import io.dz.niiuchat.domain.tables.pojos.Messages;
import io.dz.niiuchat.messaging.dto.CreateGroupOutput;
import io.dz.niiuchat.messaging.dto.GroupOutput;
import io.dz.niiuchat.messaging.dto.MessageInput;
import io.dz.niiuchat.messaging.dto.MessageOutput;
import io.dz.niiuchat.messaging.repository.ChatRepository;
import io.dz.niiuchat.messaging.repository.MessageRepository;
import io.dz.niiuchat.websocket.LiveService;
import io.dz.niiuchat.websocket.dto.LiveMessageFactory;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class MessagingService {

  private final MessagingCachedService messagingCachedService;
  private final LiveService liveService;
  private final ChatRepository chatRepository;
  private final MessageRepository messageRepository;
  private final DSLContext dslContext;

  public MessagingService(
          MessagingCachedService messagingCachedService, LiveService liveService, ChatRepository chatRepository,
          MessageRepository messageRepository,
          DSLContext dslContext
  ) {
    this.messagingCachedService = messagingCachedService;
    this.liveService = liveService;
    this.chatRepository = chatRepository;
    this.messageRepository = messageRepository;
    this.dslContext = dslContext;
  }

  public List<GroupOutput> getGroups(Long userId) {
    List<String> groupIds = chatRepository.getGroupIdsByUserId(userId);

    return chatRepository.getGroupsByGroupIds(userId, groupIds);
  }

  public CreateGroupOutput createGroup(Collection<Long> userIds) {
    var chats = new Chats();
    chats.setGroupId(UUID.randomUUID().toString());
    chats.setCreateDate(LocalDateTime.now(ZoneOffset.UTC));

    dslContext.transaction(configuration -> {

      for (Long userId : userIds) {
        chats.setUserId(userId);
        chatRepository.createGroup(configuration, chats);
      }

    });

    return CreateGroupOutput.builder().groupId(chats.getGroupId()).build();
  }

  public void insertMessageText(Long userId, MessageInput messageInput) {
    var message = new Messages();
    message.setGroupId(messageInput.getGroupId());
    message.setUserId(userId);
    message.setHasAttachment(messageInput.hasAttachmentAsByte());
    message.setMessage(messageInput.getMessage());
    message.setTimestamp(Instant.now().toEpochMilli());
    message.setCreateDate(LocalDateTime.now(ZoneOffset.UTC));

    // TODO Implement

    // Send message to websocket
    Set<Long> userIdsSet = new HashSet<>(messagingCachedService.getUserIdsForGroup(messageInput.getGroupId()));
    userIdsSet.remove(userId); // Remove self user from the notification

    var liveMessage = LiveMessageFactory.createMessageReceivedMessage(userId, messageInput.getGroupId(), messageInput.getMessage());
    liveService.sendMessage(userIdsSet, liveMessage);
  }

  public void insertMessageAttachment(Long userId) {
    // To Implement
  }

  public List<MessageOutput> getMessagesByGroupId(String groupId) {
    return messageRepository.getMessagesByGroupId(groupId);
  }
}
