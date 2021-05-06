package io.dz.niiuchat.user;

public class MissingImageException extends RuntimeException {

  public MissingImageException(String path) {
    super("Image " + path + " was not found in the storage");
  }

}
