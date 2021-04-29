package io.dz.niiuchat.storage;

import io.dz.niiuchat.domain.tables.pojos.Files;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageService {

  private final StorageRepository storageRepository;


  public StorageService(StorageRepository storageRepository) {
    this.storageRepository = storageRepository;
  }

  public void setFile(Long userId, MultipartFile file) {
    String filename = file.getOriginalFilename();
    String contentTypeName = file.getContentType();
  }

}
