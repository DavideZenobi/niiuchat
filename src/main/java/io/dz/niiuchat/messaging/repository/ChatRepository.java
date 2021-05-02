package io.dz.niiuchat.messaging.repository;

import static io.dz.niiuchat.domain.tables.Chats.CHATS;
import static io.dz.niiuchat.domain.tables.Users.USERS;

import io.dz.niiuchat.domain.tables.pojos.Chats;
import io.dz.niiuchat.messaging.dto.GroupOutput;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class ChatRepository {

  private final DSLContext dslContext;

  public ChatRepository(DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  public List<Chats> getByUserId(Long userId) {
    List<Chats> chats = dslContext.select()
        .from(CHATS)
        .where(CHATS.USER_ID.eq(userId))
        .fetchInto(Chats.class);

    return chats;
  }

  public List<String> getGroupIdsByUserId(Long userId) {
    return dslContext.select(CHATS.GROUP_ID)
        .from(CHATS)
        .where(CHATS.USER_ID.eq(userId))
        .fetchInto(String.class);
  }

  public List<Long> getUserIdsByGroupId(Long userId, List<String> groupId) {
    return dslContext.select(CHATS.USER_ID)
        .from(CHATS)
        .where(CHATS.GROUP_ID.in(groupId))
        .andNot(CHATS.USER_ID.eq(userId))
        .fetchInto(Long.class);
  }

  public List<GroupOutput> getGroupsByGroupIds(Long userId, List<String> groupIds) {
    return dslContext.select(CHATS.GROUP_ID, USERS.USERNAME)
        .from(CHATS.join(USERS).on(USERS.ID.eq(CHATS.USER_ID)))
        .where(CHATS.GROUP_ID.in(groupIds))
        .andNot(CHATS.USER_ID.eq(userId))
        .fetchInto(GroupOutput.class);
  }
}