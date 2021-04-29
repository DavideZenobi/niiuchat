package io.dz.niiuchat.storage.dto;

import io.dz.niiuchat.domain.tables.pojos.Files;

public class FilesInput {

  private String name;
  private String type;
  private String path;

  public Files toFiles() {
    Files files = new Files();
    files.setName(name);
    files.setType(type);
    files.setPath(path);

    return files;
  }

}
