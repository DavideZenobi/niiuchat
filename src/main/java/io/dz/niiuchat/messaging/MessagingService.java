package io.dz.niiuchat.messaging;

import io.dz.niiuchat.domain.tables.pojos.Users;
import io.dz.niiuchat.domain.tables.pojos.Chats;
import io.dz.niiuchat.messaging.dto.GroupOutput;
import io.dz.niiuchat.messaging.repository.ChatRepository;
import io.dz.niiuchat.user.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

@Service
public class MessagingService {

  private final ChatRepository chatRepository;
  private final UserRepository userRepository;
  private final DSLContext dslContext;

  public MessagingService(
      ChatRepository chatRepository,
      UserRepository userRepository,
      DSLContext dslContext
  ) {
    this.chatRepository = chatRepository;
    this.userRepository = userRepository;
    this.dslContext = dslContext;
  }

  public List<GroupOutput> getGroups(Long userId) {
    List<String> groupIds = chatRepository.getGroupIdsByUserId(userId);

    List<GroupOutput> groups = chatRepository.getGroupsByGroupIds(userId, groupIds);

    return groups;
  }
}