package io.dz.niiuchat.user;

import io.dz.niiuchat.authentication.UserStatus;
import io.dz.niiuchat.domain.tables.pojos.Users;
import io.dz.niiuchat.user.dto.RegisterInput;
import io.dz.niiuchat.user.register.RegisterOutput;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/public/api/users")
public class UserPublicApi {

  private final UserService userService;

  public UserPublicApi(UserService userService) {
    this.userService = userService;
  }

  @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public RegisterOutput postRegister(@Valid @RequestBody RegisterInput body) {
    Users userToCreate = body.toUser();
    userToCreate.setStatus(UserStatus.ACTIVE.toString());

    Users result = userService.createUser(userToCreate);

    return RegisterOutput.fromUser(result);
  }

}
