package io.dz.niiuchat.messaging.repository;

import io.dz.niiuchat.domain.tables.pojos.Messages;
import static io.dz.niiuchat.domain.tables.Messages.MESSAGES;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

@Repository
public class MessageRepository {

  private final DSLContext dslContext;

  public MessageRepository(
      DSLContext dslContext
  ) {
    this.dslContext = dslContext;
  }

  public void insertMessage(Messages message) {

  }

  public void insertMessage(Configuration configuration, Messages message) {
    DSLContext currentContext = (configuration != null) ?
        DSL.using(configuration) :
        dslContext;

    currentContext.insertInto(
        MESSAGES,
        MESSAGES.GROUP_ID,
        MESSAGES.USER_ID,
        MESSAGES.HAS_ATTACHMENT,
        MESSAGES.MESSAGE,
        MESSAGES.TIMESTAMP,
        MESSAGES.CREATE_DATE
    ).values(
        message.getGroupId(),
        message.getUserId(),
        message.getHasAttachment(),
        message.getMessage(),
        message.getTimestamp(),
        message.getCreateDate()
    ).execute();
  }

}
