package io.dz.niiuchat.storage.service;

import io.dz.niiuchat.domain.tables.pojos.Files;
import io.dz.niiuchat.storage.FileType;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.stereotype.Service;

@Service
public class FileService {

  private final StorageService storageService;

  public FileService(StorageService storageService) {
    this.storageService = storageService;
  }

  public Files createAvatar(Long userId, String mediaType, String path) {
    Files avatarFile = new Files();
    avatarFile.setId(storageService.getAvatarId(userId));
    avatarFile.setName("User avatar");
    avatarFile.setMediaType(mediaType);
    avatarFile.setType(FileType.AVATAR.toString());
    avatarFile.setPath(path);
    avatarFile.setCreateDate(LocalDateTime.now(ZoneOffset.UTC));

    return avatarFile;
  }

  public Files createAttachment(Long userId, String groupId, String mediaType, String path) {
    Files avatarFile = new Files();
    avatarFile.setId(storageService.getAttachmentId(userId, groupId));
    avatarFile.setName("User chat attachment");
    avatarFile.setMediaType(mediaType);
    avatarFile.setType(FileType.ATTACHMENT.toString());
    avatarFile.setPath(path);
    avatarFile.setCreateDate(LocalDateTime.now(ZoneOffset.UTC));

    return avatarFile;
  }

}
