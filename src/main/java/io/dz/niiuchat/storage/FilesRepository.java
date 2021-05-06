package io.dz.niiuchat.storage;


import static io.dz.niiuchat.domain.tables.Files.FILES;

import io.dz.niiuchat.domain.tables.pojos.Files;
import java.io.ObjectInputFilter.Config;
import java.util.Optional;
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

  public void save(Files file) {
    save(null, file);
  }

  public void save(Configuration configuration, Files file) {
    DSLContext currentContext = (configuration != null) ?
        DSL.using(configuration) :
        dslContext;

    currentContext.insertInto(
        FILES,
        FILES.ID,
        FILES.NAME,
        FILES.MEDIA_TYPE,
        FILES.TYPE,
        FILES.PATH,
        FILES.CREATE_DATE
    ).values(
        file.getId(),
        file.getName(),
        file.getMediaType(),
        file.getType(),
        file.getPath(),
        file.getCreateDate()
    ).onDuplicateKeyUpdate()
        .set(FILES.ID, file.getId())
        .set(FILES.NAME, file.getName())
        .set(FILES.MEDIA_TYPE, file.getMediaType())
        .set(FILES.TYPE, file.getType())
        .set(FILES.PATH, file.getPath())
        .set(FILES.CREATE_DATE, file.getCreateDate())
        .execute();
  }

  public Optional<Files> getPath(String id) {
    return getPath(null, id);
  }

  public Optional<Files> getPath(Configuration configuration, String id) {
    DSLContext currentContext = (configuration != null) ?
        DSL.using(configuration) :
        dslContext;

    Files result = currentContext.select()
        .from(FILES)
        .where(FILES.ID.eq(id))
        .fetchOneInto(Files.class);

    return Optional.ofNullable(result);
  }

}
