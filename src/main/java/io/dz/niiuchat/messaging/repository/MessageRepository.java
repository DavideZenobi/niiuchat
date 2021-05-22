package io.dz.niiuchat.messaging.repository;

import io.dz.niiuchat.common.PageInfo;
import io.dz.niiuchat.domain.tables.pojos.Messages;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;

import static io.dz.niiuchat.domain.tables.Messages.MESSAGES;

@Repository
public class MessageRepository {

  private final DSLContext dslContext;

  public MessageRepository(DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  public List<Messages> findAllByGroupId(String groupId, PageInfo pageInfo) {
    return dslContext.selectFrom(MESSAGES)
            .where(MESSAGES.GROUP_ID.eq(groupId))
            .orderBy(MESSAGES.CREATE_DATE.desc())
            .limit(pageInfo.getLimit())
            .offset(pageInfo.getOffset())
            .fetchInto(Messages.class);
  }

  public void insertMessage(Messages message) {
    insertMessage(null, message);
  }

  public void insertMessage(Configuration configuration, Messages message) {
    DSLContext currentContext = (configuration != null) ?
            DSL.using(configuration) :
            dslContext;

    currentContext.insertInto(
            MESSAGES,
            MESSAGES.ID,
            MESSAGES.GROUP_ID,
            MESSAGES.USER_ID,
            MESSAGES.HAS_ATTACHMENT,
            MESSAGES.MESSAGE,
            MESSAGES.TIMESTAMP,
            MESSAGES.CREATE_DATE
    ).values(
            message.getId(),
            message.getGroupId(),
            message.getUserId(),
            message.getHasAttachment(),
            message.getMessage(),
            message.getTimestamp(),
            message.getCreateDate()
    ).execute();
  }

  public void deleteMessagesByGroupId(String groupId) {
    deleteMessagesByGroupId(null, groupId);
  }

  public void deleteMessagesByGroupId(Configuration configuration, String groupId) {
    DSLContext currentContext = (configuration != null) ?
        DSL.using(configuration) :
        dslContext;

    currentContext.delete(MESSAGES)
        .where(MESSAGES.GROUP_ID.eq(groupId))
        .execute();
  }

}
