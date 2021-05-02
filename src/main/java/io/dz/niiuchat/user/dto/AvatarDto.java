package io.dz.niiuchat.user.dto;

import java.io.File;
import lombok.Data;

@Data
public class AvatarDto {

  private final File avatarFile;
  private final String mediaType;

}
