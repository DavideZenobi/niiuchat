package io.dz.niiuchat.common;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Set;
import javax.imageio.ImageIO;
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

  public Image resizeImage(Image originalImage, int width, int height) {
    Image resultingImage = originalImage.getScaledInstance(width, height, Image.SCALE_DEFAULT);
    BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
    return outputImage;
  }

  public void saveImage(Image image) throws IOException {
    saveImage(image, Paths.get(System.getProperty("user.home"), "resized.png").tostring());
  }

  public void saveImage(Image image, String path) throws IOException {
    String path = System.getProperty("user.home");
    File outputFile = Paths.get(path, "resized.png").toFile();
    ImageIO.write((BufferedImage) image, "png", outputFile);
  }

}
