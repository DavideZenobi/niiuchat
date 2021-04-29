package io.dz.niiuchat.storage;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FileSystemServiceTest {

  private FileSystemService fileSystemService;

  @BeforeEach
  void setUp() {
    fileSystemService = new FileSystemService();
  }

  @Test
  void fileIsImage() {
    File image = Paths.get(System.getProperty("user.home"), "Pictures", "497ca14763086602e93eb16bcc095318.jpg").toFile();

    boolean isImage = fileSystemService.fileIsImage(image);

    assertTrue(isImage);
  }

}
