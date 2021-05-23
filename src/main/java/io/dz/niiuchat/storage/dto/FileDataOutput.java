package io.dz.niiuchat.storage.dto;

import io.dz.niiuchat.domain.tables.pojos.Files;
import java.io.File;
import lombok.Data;

@Data
public class FileDataOutput {

  private final Files fileData;
  private final File file;

}
