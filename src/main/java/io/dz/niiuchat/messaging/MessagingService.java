package io.dz.niiuchat.messaging;

import io.dz.niiuchat.domain.tables.pojos.Messages;
import io.dz.niiuchat.domain.tables.pojos.Users;
import io.dz.niiuchat.domain.tables.pojos.Chats;
import io.dz.niiuchat.messaging.dto.CreateGroupOutput;
import io.dz.niiuchat.messaging.dto.GroupOutput;
import io.dz.niiuchat.messaging.dto.MessageInput;
import io.dz.niiuchat.messaging.dto.MessageOutput;
import io.dz.niiuchat.messaging.repository.ChatRepository;
import io.dz.niiuchat.messaging.repository.MessageRepository;
import io.dz.niiuchat.user.repository.UserRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

@Service
public class MessagingService {

  private final ChatRepository chatRepository;
  private final UserRepository userRepository;
  private final MessageRepository messageRepository;
  private final DSLContext dslContext;

  public MessagingService(
      ChatRepository chatRepository,
      UserRepository userRepository,
      MessageRepository messageRepository,
      DSLContext dslContext
  ) {
    this.chatRepository = chatRepository;
    this.userRepository = userRepository;
    this.messageRepository = messageRepository;
    this.dslContext = dslContext;
  }

  public List<GroupOutput> getGroups(Long userId) {
    List<String> groupIds = chatRepository.getGroupIdsByUserId(userId);

    List<GroupOutput> groups = chatRepository.getGroupsByGroupIds(userId, groupIds);

    return groups;
  }

  public CreateGroupOutput createGroup(Collection<Long> userIds) {

    Chats chats = new Chats();
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
    Messages message = new Messages();
    message.setGroupId(messageInput.getGroupId());
    message.setUserId(userId);
    message.setHasAttachment((byte) (messageInput.getHasAttachment() ? 1 : 0));
    message.setMessage(messageInput.getMessage());
    message.setTimestamp(Instant.now().toEpochMilli());
    message.setCreateDate(LocalDateTime.now(ZoneOffset.UTC));
  }

  public void insertMessageAttachment(Long userId) {

  }

  public List<MessageOutput> getMessagesByGroupId(String groupId) {
    return messageRepository.getMessagesByGroupId(groupId);
  }
}
