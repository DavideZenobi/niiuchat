package io.dz.niiuchat.common;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

  private final TikaConfig tikaConfig = TikaConfig.getDefaultConfig();
  private final Set<String> acceptedImages = Set.of("image/jpeg", "image/png");

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

}
