package io.dz.niiuchat.storage;


import static io.dz.niiuchat.domain.tables.Files.FILES;

import io.dz.niiuchat.domain.tables.pojos.Files;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

@Repository
public class FilesRepository {

  private final DSLContext dslContext;

  public FilesRepository(DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  public String createFile(Files file) {
    return createFile(null, file);
  }

  public String createFile(Configuration configuration, Files file) {
    DSLContext currentContext = (configuration != null) ?
        DSL.using(configuration) :
        dslContext;

    return currentContext.insertInto(
        FILES,
        FILES.NAME,
        FILES.MEDIA_TYPE,
        FILES.TYPE,
        FILES.PATH,
        FILES.CREATE_DATE
    ).values(
        file.getName(),
        file.getMediaType(),
        file.getType(),
        file.getPath(),
        file.getCreateDate()
    )
        .returningResult(FILES.ID)
        .fetchOne().value1();

  }

  public void updateFile(String mediaType, String id) {
    updateFile(null, mediaType, id);
  }

  public void updateFile(Configuration configuration, String mediaType, String id) {
    DSLContext currentContext = (configuration != null) ?
        DSL.using(configuration) :
        dslContext;

    currentContext.update(FILES)
        .set(FILES.MEDIA_TYPE, mediaType)
        .where(FILES.ID.eq(id))
        .execute();
  }

}
