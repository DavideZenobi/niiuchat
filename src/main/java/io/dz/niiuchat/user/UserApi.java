package io.dz.niiuchat.user;


import io.dz.niiuchat.authentication.NiiuUser;
import io.dz.niiuchat.domain.tables.pojos.Users;
import java.security.Principal;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
