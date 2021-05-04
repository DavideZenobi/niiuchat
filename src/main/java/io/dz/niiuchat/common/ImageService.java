package io.dz.niiuchat.common;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import javax.imageio.ImageIO;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

  private final TikaConfig tikaConfig = TikaConfig.getDefaultConfig();
  private final Set<String> acceptedImages = Set.of("image/jpeg", "image/png");
  private final Path avatarsDirectory = Paths.get(System.getProperty("user.home"), "niiu", "avatars");

  public boolean isImage(MediaType mediaType) {
    return acceptedImages.contains(mediaType.toString());
  }

  public MediaType getMediaType(InputStream inputStream) throws IOException {
    return tikaConfig.getMimeRepository().detect(inputStream, new Metadata());
  }

  public BufferedImage resizeAvatar(Image originalImage) {
    return resizeImage(originalImage, 64, 64);
  }

  public BufferedImage resizeImage(Image originalImage, int width, int height) {
    BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    Graphics2D graphics2D = resizedImage.createGraphics();
    graphics2D.drawImage(originalImage, 0, 0, width, height, null);
    graphics2D.dispose();

    return resizedImage;
  }

  public String saveAvatar(BufferedImage image, String format, Long userId) throws IOException {
    String imageName = userId + "." + format;

    if (Files.notExists(avatarsDirectory)) {
      Files.createDirectories(avatarsDirectory);
    }

    String imageFullPath = avatarsDirectory.resolve(imageName).toString();

    saveImage(image, imageFullPath, format);

    return imageName;
  }
/*
  public void saveAvatar(BufferedImage image, String format, ImagePaths avatarPaths) throws IOException {
    if (Files.notExists(AVATARS_ABSOLUTE_PATH)) {
      Files.createDirectories(AVATARS_ABSOLUTE_PATH);
    }

    saveImage(image, avatarPaths.getAbsolutePath(), format);
  }
*/
  public void saveImage(BufferedImage image, String path, String format) throws IOException {
    File outputFile = new File(path);
    ImageIO.write(image, format, outputFile);
  }

}
