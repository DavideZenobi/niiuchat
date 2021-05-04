package io.dz.niiuchat.storage;


import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class FilesRepository {

  private final DSLContext dslContext;

  public FilesRepository(DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  public void createFile() {

  }

}
