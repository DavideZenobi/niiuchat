package io.dz.niiuchat.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.apache.tika.mime.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

  private static final String PNG_RESOURCE_PATH = "default_avatar.png";
  private static final String PDF_RESOURCE_PATH = "dummy.pdf";

  private ImageService imageService;

  @BeforeEach
  void init() {
    imageService = new ImageService();
  }

  @Test
  @DisplayName("Check if a PNG is an accepted image mediaType")
  void pngIsAcceptedMediaType() {
    boolean pngAccepted = imageService.isAcceptedImage(MediaType.image("png"));
    assertTrue(pngAccepted);
  }

  @Test
  @DisplayName("Check if a JPG / JPEG is an accepted image mediaType")
  void jpgIsAcceptedMediaType() {
    boolean jpgAccepted = imageService.isAcceptedImage(MediaType.image("jpg"));
    assertTrue(jpgAccepted);

    boolean jpegAccepted = imageService.isAcceptedImage(MediaType.image("jpeg"));
    assertTrue(jpegAccepted);
  }

  @Test
  @DisplayName("Check if a PDF is NOT an accepted image mediaType")
  void pdfIsNotAcceptedMediaType() {
    boolean pdfAccepted = imageService.isAcceptedImage(MediaType.application("pdf"));
    assertFalse(pdfAccepted);
  }

  @Test
  @DisplayName("Get image media type")
  void getImageMediaType() throws IOException {
    InputStream imageStream = this.getClass().getClassLoader().getResourceAsStream(PNG_RESOURCE_PATH);
    assert imageStream != null;

    MediaType imageMediaType = imageService.getMediaType(imageStream);

    assertEquals(MediaType.image("png"), imageMediaType);

    imageStream.close();
  }

  @Test
  @DisplayName("Get pdf media type")
  void getPdfMediaType() throws IOException {
    InputStream pdfStream = this.getClass().getClassLoader().getResourceAsStream(PDF_RESOURCE_PATH);
    assert pdfStream != null;

    MediaType pdfMediaType = imageService.getMediaType(pdfStream);

    assertEquals(MediaType.application("pdf"), pdfMediaType);

    pdfStream.close();
  }

  @Test
  @DisplayName("Return the image with the expected avatar size")
  void resizeAvatarReturnCorrectImageSize() throws IOException {
    InputStream imageStream = this.getClass().getClassLoader().getResourceAsStream(PNG_RESOURCE_PATH);
    assert imageStream != null;

    BufferedImage resizedImage = imageService.resizeAvatar(ImageIO.read(imageStream));

    assertEquals(ImageService.AVATAR_DEFAULT_WIDTH, resizedImage.getWidth());
    assertEquals(ImageService.AVATAR_DEFAULT_HEIGHT, resizedImage.getHeight());

    imageStream.close();
  }

}
