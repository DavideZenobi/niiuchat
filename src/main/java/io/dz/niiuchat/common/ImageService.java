package io.dz.niiuchat.common;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

  private final Tika tika = new Tika();
  private final Set<String> acceptedImages = Set.of("image/jpeg", "image/png");

  public boolean isImage(InputStream inputStream) throws IOException {
    String mimeType = tika.detect(inputStream);

    return acceptedImages.contains(mimeType);
  }

  public Image resizeImage(Image image) {
    return null;
  }

}
