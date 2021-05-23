package io.dz.niiuchat.messaging.repository;

import static io.dz.niiuchat.domain.Tables.ATTACHMENTS;
import static io.dz.niiuchat.domain.tables.Messages.MESSAGES;

import io.dz.niiuchat.common.PageInfo;
import io.dz.niiuchat.domain.tables.pojos.Attachments;
import io.dz.niiuchat.domain.tables.pojos.Messages;
import io.dz.niiuchat.messaging.dto.MessageDto;
import java.util.List;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

@Repository
public class MessageRepository {

  private final DSLContext dslContext;

  public MessageRepository(DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  public List<MessageDto> findAllByGroupId(String groupId, PageInfo pageInfo) {
    return dslContext.select(MESSAGES.fields())
        .select(ATTACHMENTS.fields())
        .from(MESSAGES)
        .leftJoin(ATTACHMENTS).on(ATTACHMENTS.MESSAGE_ID.eq(MESSAGES.ID))
        .where(MESSAGES.GROUP_ID.eq(groupId))
        .orderBy(MESSAGES.TIMESTAMP.desc())
        .limit(pageInfo.getLimit())
        .offset(pageInfo.getOffset())
        .fetch(record -> {
          var message = new Messages();
          message.setId(record.get(MESSAGES.ID));
          message.setGroupId(record.get(MESSAGES.GROUP_ID));
          message.setUserId(record.get(MESSAGES.USER_ID));
          message.setHasAttachment(record.get(MESSAGES.HAS_ATTACHMENT));
          message.setMessage(record.get(MESSAGES.MESSAGE));
          message.setTimestamp(record.get(MESSAGES.TIMESTAMP));
          message.setCreateDate(record.get(MESSAGES.CREATE_DATE));

          Attachments attachment = null;

          if (record.get(ATTACHMENTS.ID) != null) {
            attachment = new Attachments();
            attachment.setId(record.get(ATTACHMENTS.ID));
            attachment.setMessageId(record.get(ATTACHMENTS.MESSAGE_ID));
            attachment.setType(record.get(ATTACHMENTS.TYPE));
            attachment.setFileId(record.get(ATTACHMENTS.FILE_ID));
            attachment.setCreateDate(record.get(ATTACHMENTS.CREATE_DATE));
          }

          return new MessageDto(message, attachment);
        });
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
