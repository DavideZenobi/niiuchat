package io.dz.niiuchat.storage;

import java.nio.file.Path;
import java.nio.file.Paths;

public class StorageVars {

  private StorageVars() { }

  public static final Path NIIU_PATH = Paths.get("niiu");

  public static final Path ROOT_ABSOLUTE_PATH = Paths.get(System.getProperty("user.home"));
  public static final Path NIIU_ABSOLUTE_PATH = ROOT_ABSOLUTE_PATH.resolve(NIIU_PATH);

  public static final Path AVATARS_PATH = Paths.get("avatars");
  public static final Path AVATARS_RELATIVE_PATH = NIIU_PATH.resolve(AVATARS_PATH);
  public static final Path AVATARS_ABSOLUTE_PATH = NIIU_ABSOLUTE_PATH.resolve(AVATARS_PATH);

  public static final Path ATTACHMENTS_PATH = Paths.get("attachments");
  public static final Path ATTACHMENTS_RELATIVE_PATH = NIIU_PATH.resolve(ATTACHMENTS_PATH);
  public static final Path ATTACHMENTS_ABSOLUTE_PATH = NIIU_ABSOLUTE_PATH.resolve(ATTACHMENTS_PATH);
}
