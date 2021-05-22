package io.dz.niiuchat.messaging.repository;

import static io.dz.niiuchat.domain.tables.Attachments.ATTACHMENTS;

import io.dz.niiuchat.domain.tables.pojos.Attachments;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

@Repository
public class AttachmentRepository {

  private final DSLContext dslContext;

  public AttachmentRepository(DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  public void create(Attachments attachments) {
    create(null, attachments);
  }

  public void create(Configuration configuration, Attachments attachment) {
    DSLContext currentContext = (configuration != null) ?
        DSL.using(configuration) :
        dslContext;

    currentContext.insertInto(
        ATTACHMENTS,
        ATTACHMENTS.ID,
        ATTACHMENTS.MESSAGE_ID,
        ATTACHMENTS.TYPE,
        ATTACHMENTS.FILE_ID,
        ATTACHMENTS.CREATE_DATE
    ).values(
        attachment.getId(),
        attachment.getMessageId(),
        attachment.getType(),
        attachment.getFileId(),
        attachment.getCreateDate()
    ).execute();
  }

}
