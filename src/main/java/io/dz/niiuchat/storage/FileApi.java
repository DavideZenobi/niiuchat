package io.dz.niiuchat.storage;

import io.dz.niiuchat.authentication.NiiuUser;
import io.dz.niiuchat.storage.service.FileService;
import java.security.Principal;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/files")
public class FileApi {

  private final FileService fileService;

  public FileApi(FileService fileService) { this.fileService = fileService; }

  @GetMapping(path = "/{fileId}")
  public ResponseEntity<FileSystemResource> getFile(
      Principal principal,
      @PathVariable String fileId
  ) {
    var fileDataOutputOptional = fileService.getFileData(fileId, NiiuUser.from(principal).getUser().getId());

    if (fileDataOutputOptional.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    var fileDataOutput = fileDataOutputOptional.get();

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, fileDataOutput.getFileData().getMediaType());
    headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileDataOutput.getFile().length()));
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDataOutput.getFileData().getName() + "\"");

    return new ResponseEntity<>(new FileSystemResource(fileDataOutput.getFile()), headers, HttpStatus.OK);
  }

}
