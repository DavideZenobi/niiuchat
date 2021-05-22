package io.dz.niiuchat.messaging;

import io.dz.niiuchat.common.PageInfo;
import io.dz.niiuchat.domain.tables.pojos.Chats;
import io.dz.niiuchat.domain.tables.pojos.Messages;
import io.dz.niiuchat.messaging.dto.CreateGroupOutput;
import io.dz.niiuchat.messaging.dto.CreateMessageInput;
import io.dz.niiuchat.messaging.dto.GroupOutput;
import io.dz.niiuchat.messaging.repository.ChatRepository;
import io.dz.niiuchat.messaging.repository.MessageRepository;
import io.dz.niiuchat.websocket.LiveService;
import io.dz.niiuchat.websocket.dto.LiveMessageFactory;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

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

  public CreateGroupOutput createGroup(List<Long> userIds, Long currentUser) {
    var chats = new Chats();
    chats.setGroupId(UUID.randomUUID().toString());
    chats.setCreateDate(LocalDateTime.now(ZoneOffset.UTC));

    dslContext.transaction(configuration -> {
      boolean alreadyExists = chatRepository.chatAlreadyExist(configuration, userIds.get(0), userIds.get(1));

      if (alreadyExists) {
        throw new RuntimeException("Chat cannot be created. Group already exists.");
      }

      for (Long userId : userIds) {
        chats.setUserId(userId);
        chatRepository.createGroup(configuration, chats);
      }

      userIds.remove(currentUser);

      var liveMessage = LiveMessageFactory.createGroupCreatedMessage(chats.getGroupId());
      liveService.sendMessage(userIds, liveMessage);
    });

    return CreateGroupOutput.builder().groupId(chats.getGroupId()).build();
  }

  public Messages insertMessageText(Long userId, CreateMessageInput createMessageInput) {
    var now = Instant.now();

    var message = new Messages();
    message.setId(UUID.randomUUID().toString());
    message.setGroupId(createMessageInput.getGroupId());
    message.setUserId(userId);
    message.setHasAttachment(createMessageInput.hasAttachmentAsByte());
    message.setMessage(createMessageInput.getMessage());
    message.setTimestamp(now.toEpochMilli());
    message.setCreateDate(LocalDateTime.ofInstant(now, ZoneOffset.UTC));

    // TODO Implement

    messageRepository.insertMessage(message);

    // Send message to websocket
    Set<Long> userIdsSet = new HashSet<>(messagingCachedService.getUserIdsForGroup(createMessageInput.getGroupId()));
    userIdsSet.remove(userId); // Remove self user from the notification

    var liveMessage = LiveMessageFactory.createMessageReceivedMessage(userId, createMessageInput.getGroupId(), createMessageInput.getMessage());
    liveService.sendMessage(userIdsSet, liveMessage);

    return message;
  }

  public void insertMessageAttachment(Long userId) {
    // To Implement
  }

  public List<Messages> getMessagesByGroupId(Long userId, String groupId, PageInfo pageInfo) {
    Set<Long> userIdsInGroup = messagingCachedService.getUserIdsForGroup(groupId);

    if (!userIdsInGroup.contains(userId)) {
      throw new RuntimeException("User " + userId + " does not belongs to selected group " + groupId);
    }

    return messageRepository.findAllByGroupId(groupId, pageInfo);
  }

  public void deleteChat(Long userId, String groupId) {
    Set<Long> userIdsInGroup = messagingCachedService.getUserIdsForGroup(groupId);

    if (!userIdsInGroup.contains(userId)) {
      throw new RuntimeException("User " + userId + " does not belongs to selected group " + groupId);
    }

    dslContext.transaction(configuration -> {
      messageRepository.deleteMessagesByGroupId(configuration, groupId);
      chatRepository.deleteChat(configuration, groupId);
    });

    var liveMessage = LiveMessageFactory.createGroupDeletedMessage(groupId);
    liveService.sendMessage(userIdsInGroup, liveMessage);
  }

}
