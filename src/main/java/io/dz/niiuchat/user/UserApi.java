package io.dz.niiuchat.user;

import io.dz.niiuchat.authentication.NiiuUser;
import io.dz.niiuchat.domain.tables.pojos.Users;
import io.dz.niiuchat.user.dto.AvatarDto;
import io.dz.niiuchat.user.dto.UpdateUserDataInput;
import io.dz.niiuchat.user.dto.UpdateUserPasswordInput;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/api/users")
public class UserApi {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserApi.class);

  private final UserService userService;

  public UserApi(UserService userService) {
    this.userService = userService;
  }

  @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Users> getAll() {

    List<Users> result = userService.getAll();

    for(Users user : result) {
      user.setPassword(null);
    }

    return result;
  }

  @GetMapping(path = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
  public Users getProfile(Principal principal) {
    return NiiuUser.from(principal).getUser();
  }

  @GetMapping(path = "/{userId}/avatar")
  public ResponseEntity<FileSystemResource> getProfile(
      Principal principal,
      @PathVariable Long userId
  ) {
    var avatarOptional = userService.findAvatar(userId);

    if (avatarOptional.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    AvatarDto avatarData = avatarOptional.get();

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, avatarData.getMediaType());
    headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(avatarData.getAvatarFile().length()));

    return new ResponseEntity<>(new FileSystemResource(avatarData.getAvatarFile()), headers, HttpStatus.OK);
  }

  @PostMapping(path = "/update/data", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> postUpdateUserData(
      Principal principal,
      @Valid @RequestBody UpdateUserDataInput body
  ) {
    Users userToUpdate = body.toUser();
    userToUpdate.setId(NiiuUser.from(principal).getUser().getId());

    userService.updateData(userToUpdate);

    return ResponseEntity.noContent().build();
  }

  @PostMapping(path = "/update/password", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> postUpdateUserPassword(
      Principal principal,
      @Valid @RequestBody UpdateUserPasswordInput body
  ) {
    userService.updatePassword(NiiuUser.from(principal).getUser().getId(), body.getPassword());

    return ResponseEntity.noContent().build();
  }

  @PostMapping(path = "/update/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Object> postUpdateAvatar(
      Principal principal,
      @RequestParam("avatar") MultipartFile file
  ) {
    try (InputStream fileStream = new BufferedInputStream(file.getInputStream(), (int) file.getSize())) {
      userService.upsertAvatar(fileStream, NiiuUser.from(principal).getUser().getId());

      return ResponseEntity.noContent().build();
    } catch (IOException e) {
      LOGGER.error("Error uploading Avatar", e);
      return ResponseEntity.badRequest().build();
    }
  }

}
