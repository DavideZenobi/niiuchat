package io.dz.niiuchat.storage.repository;

import static io.dz.niiuchat.domain.tables.Files.FILES;

import io.dz.niiuchat.domain.tables.pojos.Files;
import java.util.Optional;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

@Repository
public class FileRepository {

  private final DSLContext dslContext;

  public FileRepository(DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  public Optional<Files> findOne(String fileId) {
    return findOne(null, fileId);
  }

  public Optional<Files> findOne(Configuration configuration, String fileId) {
    DSLContext currentContext = (configuration != null) ?
        DSL.using(configuration) :
        dslContext;

    Files result = currentContext.select()
        .from(FILES)
        .where(FILES.ID.eq(fileId))
        .fetchOneInto(Files.class);

    return Optional.ofNullable(result);
  }

  public Files save(Files file) {
    return save(null, file);
  }

  public Files save(Configuration configuration, Files file) {
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
        .set(FILES.NAME, file.getName())
        .set(FILES.MEDIA_TYPE, file.getMediaType())
        .set(FILES.TYPE, file.getType())
        .set(FILES.PATH, file.getPath())
        .set(FILES.CREATE_DATE, file.getCreateDate())
        .execute();

    return file;
  }

}
