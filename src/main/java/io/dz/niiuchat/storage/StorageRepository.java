package io.dz.niiuchat.storage;


import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class StorageRepository {

  private final DSLContext dslContext;

  public StorageRepository(DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  public void createFile() {

  }

}
