package io.dz.niiuchat.messaging;

import io.dz.niiuchat.common.PageInfo;
import io.dz.niiuchat.domain.tables.pojos.Attachments;
import io.dz.niiuchat.domain.tables.pojos.Chats;
import io.dz.niiuchat.domain.tables.pojos.Messages;
import io.dz.niiuchat.messaging.dto.CreateGroupOutput;
import io.dz.niiuchat.messaging.dto.CreateMessageInput;
import io.dz.niiuchat.messaging.dto.GroupOutput;
import io.dz.niiuchat.messaging.repository.AttachmentRepository;
import io.dz.niiuchat.messaging.repository.ChatRepository;
import io.dz.niiuchat.messaging.repository.MessageRepository;
import io.dz.niiuchat.storage.repository.FileRepository;
import io.dz.niiuchat.storage.service.FileService;
import io.dz.niiuchat.storage.service.StorageService;
import io.dz.niiuchat.websocket.LiveService;
import io.dz.niiuchat.websocket.dto.LiveMessageFactory;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MessagingService {

  private static final Logger LOGGER = LoggerFactory.getLogger(MessagingService.class);

  private final MessagingCachedService messagingCachedService;
  private final LiveService liveService;
  private final FileService fileService;
  private final StorageService storageService;
  private final ChatRepository chatRepository;
  private final MessageRepository messageRepository;
  private final FileRepository fileRepository;
  private final AttachmentRepository attachmentRepository;
  private final DSLContext dslContext;

  public MessagingService(
      MessagingCachedService messagingCachedService,
      LiveService liveService,
      FileService fileService,
      StorageService storageService,
      ChatRepository chatRepository,
      MessageRepository messageRepository,
      FileRepository fileRepository,
      AttachmentRepository attachmentRepository,
      DSLContext dslContext
  ) {
    this.messagingCachedService = messagingCachedService;
    this.liveService = liveService;
    this.fileService = fileService;
    this.storageService = storageService;
    this.chatRepository = chatRepository;
    this.messageRepository = messageRepository;
    this.fileRepository = fileRepository;
    this.attachmentRepository = attachmentRepository;
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

  public Messages insertMessage(Long userId, CreateMessageInput createMessageInput) {
    var now = Instant.now();

    var message = new Messages();
    message.setId(UUID.randomUUID().toString());
    message.setGroupId(createMessageInput.getGroupId());
    message.setUserId(userId);
    message.setHasAttachment(createMessageInput.hasAttachmentAsByte());
    message.setMessage(createMessageInput.getMessage());
    message.setTimestamp(now.toEpochMilli());
    message.setCreateDate(LocalDateTime.ofInstant(now, ZoneOffset.UTC));

    dslContext.transaction(configuration -> {
      messageRepository.insertMessage(configuration, message);

      if (Boolean.TRUE.equals(createMessageInput.hasAttachment())) {
        try (InputStream fileStream = new BufferedInputStream(createMessageInput.getAttachment().getInputStream(), (int) createMessageInput.getAttachment().getSize())) {
          var attachmentMediaType = storageService.getMediaType(fileStream);
          var attachmentId = storageService.getAttachmentId(userId, createMessageInput.getGroupId());
          var attachmentsPaths = storageService.getAttachmentPaths(attachmentMediaType.getSubtype(), attachmentId);
          var file = fileService.createAttachment(userId, createMessageInput.getGroupId(), attachmentMediaType.toString(), attachmentsPaths.getRelativePath());

          fileRepository.save(configuration, file);

          var attachment = new Attachments();
          attachment.setId(UUID.randomUUID().toString());
          attachment.setMessageId(message.getId());
          attachment.setType("");
          attachment.setFileId(file.getId());
          attachment.setCreateDate(LocalDateTime.now(ZoneOffset.UTC));

          attachmentRepository.create(configuration, attachment);

          storageService.saveAttachment(fileStream.readAllBytes(), attachmentsPaths);
        } catch (IOException e) {
          LOGGER.error("Error storing attachment", e);

          throw new RuntimeException("Error creating message with attachment");
        }
      }
    });

    // Send message to websocket
    Set<Long> userIdsSet = new HashSet<>(messagingCachedService.getUserIdsForGroup(createMessageInput.getGroupId()));
    userIdsSet.remove(userId); // Remove self user from the notification

    var liveMessage = LiveMessageFactory.createMessageReceivedMessage(
        userId,
        createMessageInput.getGroupId(),
        createMessageInput.getMessage(),
        createMessageInput.hasAttachment()
    );

    liveService.sendMessage(userIdsSet, liveMessage);

    return message;
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
