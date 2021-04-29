package io.dz.niiuchat.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

  private ImageService imageService;

  @BeforeEach
  void init() {
    imageService = new ImageService();
  }

  @Test
  @DisplayName(value = "Check if file is an image")
  void fileIsImage() {

  }

  @Test
  @DisplayName(value = "Check if file is null false is returned")
  void nullFileReturnFalse() {

  }

}
