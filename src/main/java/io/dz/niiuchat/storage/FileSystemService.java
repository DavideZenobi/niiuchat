package io.dz.niiuchat.storage;

import java.io.File;
import org.springframework.stereotype.Service;

@Service
public class FileSystemService {

  public boolean fileIsImage(File file) {
    return file.getAbsolutePath().endsWith(".jpg");
  }

}
