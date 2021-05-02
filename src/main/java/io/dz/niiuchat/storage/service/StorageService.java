package io.dz.niiuchat.storage.service;

import static io.dz.niiuchat.storage.StorageVars.AVATARS_ABSOLUTE_PATH;
import static io.dz.niiuchat.storage.StorageVars.AVATARS_RELATIVE_PATH;
import static io.dz.niiuchat.storage.StorageVars.ROOT_ABSOLUTE_PATH;

import io.dz.niiuchat.storage.dto.ImagePaths;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StorageService {

  private static final Logger LOGGER = LoggerFactory.getLogger(StorageService.class);

  public ImagePaths getAvatarPaths(String format, Long userId) {
    String imageName = userId + "." + format;

    String relativePath = AVATARS_RELATIVE_PATH.resolve(imageName).toString();
    String absolutePath = AVATARS_ABSOLUTE_PATH.resolve(imageName).toString();

    return new ImagePaths(relativePath, absolutePath);
  }

  public void saveAvatar(BufferedImage image, String format, ImagePaths avatarPaths) throws IOException {
    if (Files.notExists(AVATARS_ABSOLUTE_PATH)) {
      Files.createDirectories(AVATARS_ABSOLUTE_PATH);
    }

    saveImage(image, avatarPaths.getAbsolutePath(), format);
  }

  public void saveImage(BufferedImage image, String path, String format) throws IOException {
    File outputFile = new File(path);
    ImageIO.write(image, format, outputFile);
  }

  public void deleteAvatar(String relativePath) {
    try {
      Files.deleteIfExists(ROOT_ABSOLUTE_PATH.resolve(relativePath));
      LOGGER.info("Deleted avatar {}", relativePath);
    } catch (IOException e) {
      LOGGER.error("Error deleting avatar {}", relativePath, e);
    }
  }

  public String getAvatarId(Long userId) {
    return "avatar_" + userId;
  }

}
