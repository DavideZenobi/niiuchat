package io.dz.niiuchat.storage;

import java.io.File;
import org.springframework.stereotype.Service;

@Service
public class FileSystemService {

  public boolean fileIsImage(File file) {
    if(!file.exists()) {
      return false;
    }

    return file.getAbsolutePath().endsWith(".jpg");
  }

}
