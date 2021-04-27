package io.dz.niiuchat.user;

import io.dz.niiuchat.authentication.NiiuUser;
import io.dz.niiuchat.domain.tables.pojos.Users;
import io.dz.niiuchat.user.dto.UpdateUserDataInput;
import io.dz.niiuchat.user.dto.UpdateUserPasswordInput;
import java.security.Principal;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/api/users")
public class UserApi {

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
    return ResponseEntity.noContent().build();
  }

}
