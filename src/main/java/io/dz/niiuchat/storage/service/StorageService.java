package io.dz.niiuchat.storage.service;

import static io.dz.niiuchat.storage.StorageVars.ATTACHMENTS_ABSOLUTE_PATH;
import static io.dz.niiuchat.storage.StorageVars.ATTACHMENTS_RELATIVE_PATH;
import static io.dz.niiuchat.storage.StorageVars.AVATARS_ABSOLUTE_PATH;
import static io.dz.niiuchat.storage.StorageVars.AVATARS_RELATIVE_PATH;
import static io.dz.niiuchat.storage.StorageVars.ROOT_ABSOLUTE_PATH;

import io.dz.niiuchat.storage.dto.FilePaths;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import javax.imageio.ImageIO;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StorageService {

  private static final Logger LOGGER = LoggerFactory.getLogger(StorageService.class);
  private static final TikaConfig TIKA_CONFIG = TikaConfig.getDefaultConfig();

  public FilePaths getAvatarPaths(String format, Long userId) {
    String imageName = userId + "." + format;

    String relativePath = AVATARS_RELATIVE_PATH.resolve(imageName).toString();
    String absolutePath = AVATARS_ABSOLUTE_PATH.resolve(imageName).toString();

    return new FilePaths(relativePath, absolutePath);
  }

  public void saveAvatar(BufferedImage image, String format, FilePaths avatarPaths) throws IOException {
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

  public File getFileFromRelativePath(String filePath) {
    return ROOT_ABSOLUTE_PATH.resolve(filePath).toFile();
  }

  public MediaType getMediaType(InputStream inputStream) throws IOException {
    return TIKA_CONFIG.getMimeRepository().detect(inputStream, new Metadata());
  }

  public FilePaths getAttachmentPaths(String format, String attachmentId) {
    String attachmentName = attachmentId + "." + format;

    String relativePath = ATTACHMENTS_RELATIVE_PATH.resolve(attachmentName).toString();
    String absolutePath = ATTACHMENTS_ABSOLUTE_PATH.resolve(attachmentName).toString();

    return new FilePaths(relativePath, absolutePath);
  }

  public String getAttachmentId(Long userId, String groupId) {
    return userId + "_" + groupId + "_" + Instant.now().toEpochMilli();
  }

  public void saveAttachment(byte[] attachment, FilePaths attachmentPaths) throws IOException {
    if (Files.notExists(ATTACHMENTS_ABSOLUTE_PATH)) {
      Files.createDirectories(ATTACHMENTS_ABSOLUTE_PATH);
    }

    saveAttachment(attachment, attachmentPaths.getAbsolutePath());
  }

  public void saveAttachment(byte[] attachment, String path) throws IOException {
    Files.write(Paths.get(path), attachment);
  }

}
