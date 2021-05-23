package io.dz.niiuchat.storage.service;

import io.dz.niiuchat.domain.tables.pojos.Files;
import io.dz.niiuchat.storage.FileType;
import io.dz.niiuchat.storage.dto.FileDataOutput;
import io.dz.niiuchat.storage.repository.FileRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class FileService {

  private final StorageService storageService;
  private final FileRepository fileRepository;

  public FileService(
      StorageService storageService,
      FileRepository fileRepository
  ) {
    this.storageService = storageService;
    this.fileRepository = fileRepository;
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

  public Files createAttachment(String attachmentId, String name, String mediaType, String path) {
    Files avatarFile = new Files();
    avatarFile.setId(attachmentId);
    avatarFile.setName(name);
    avatarFile.setMediaType(mediaType);
    avatarFile.setType(FileType.ATTACHMENT.toString());
    avatarFile.setPath(path);
    avatarFile.setCreateDate(LocalDateTime.now(ZoneOffset.UTC));

    return avatarFile;
  }

  public Optional<FileDataOutput> getFileData(String fileId, Long id) {
    var fileOptional = fileRepository.findOne(fileId);

    if (fileOptional.isEmpty()) {
      return Optional.empty();
    }

    Files fileRecord = fileOptional.get();
    var file = storageService.getFileFromRelativePath(fileRecord.getPath());

    if (!file.exists()) {
      throw new RuntimeException(file.getAbsolutePath());
    }



    return Optional.of(new FileDataOutput(fileRecord, file));
  }

}
