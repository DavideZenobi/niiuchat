package io.dz.niiuchat.common;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.springframework.stereotype.Service;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

@Service
public class ImageService {

  public static final int AVATAR_DEFAULT_WIDTH = 64;
  public static final int AVATAR_DEFAULT_HEIGHT = 64;

  private static final TikaConfig TIKA_CONFIG = TikaConfig.getDefaultConfig();
  private static final Set<MediaType> ACCEPTED_IMAGES = Set.of(
          MediaType.image("jpg"),
          MediaType.image("jpeg"),
          MediaType.image("png")
  );

  public boolean isAcceptedImage(MediaType mediaType) {
    return ACCEPTED_IMAGES.contains(mediaType);
  }

  public MediaType getMediaType(InputStream inputStream) throws IOException {
    return TIKA_CONFIG.getMimeRepository().detect(inputStream, new Metadata());
  }

  public BufferedImage resizeAvatar(Image originalImage) {
    return resizeImage(originalImage, AVATAR_DEFAULT_WIDTH, AVATAR_DEFAULT_HEIGHT);
  }

  public BufferedImage resizeImage(Image originalImage, int width, int height) {
    BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    Graphics2D graphics2D = resizedImage.createGraphics();
    graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    graphics2D.drawImage(originalImage, 0, 0, width, height, null);
    graphics2D.dispose();

    return resizedImage;
  }

}
