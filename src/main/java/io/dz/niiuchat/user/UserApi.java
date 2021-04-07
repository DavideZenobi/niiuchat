package io.dz.niiuchat.user;


import io.dz.niiuchat.domain.tables.pojos.Users;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/private/api/users")
public class UserApi {

  private final UserService userService;

  public UserApi(UserService userService) {
    this.userService = userService;
  }

  @GetMapping(path = "/")
  public List<Users> getAll() {

    List<Users> result = userService.getAll();

    for(Users user : result) {
      user.setPassword(null);
    }

    return result;
  }
}
