package io.dz.niiuchat.user;

import io.dz.niiuchat.authentication.UserStatus;
import io.dz.niiuchat.domain.tables.pojos.Users;
import java.time.LocalDateTime;

public class UserTestUtil {

  public static final long USER_ID = 1L;
  public static final String USER_USERNAME = "test user";
  public static final String USER_EMAIL = "test_user@niiuchat.com";
  public static final String USER_PLAIN_PASSWORD = "random";
  public static final String USER_ENCRYPTED_PASSWORD = "encoded";
  public static final String USER_STATUS = UserStatus.ACTIVE.toString();
  public static final LocalDateTime USER_CREATE_DATE = LocalDateTime.now();
  public static final LocalDateTime USER_UPDATE_DATE = LocalDateTime.now().plusDays(5);

  public static Users stubbedUser() {
    return stubbedUser(true);
  }

  public static Users stubbedUser(boolean plainPassword) {
    Users user = new Users();
    user.setId(USER_ID);
    user.setUsername(USER_USERNAME);
    user.setEmail(USER_EMAIL);
    user.setPassword(plainPassword ? USER_PLAIN_PASSWORD : USER_ENCRYPTED_PASSWORD);
    user.setStatus(USER_STATUS);
    user.setCreateDate(USER_CREATE_DATE);
    user.setUpdateDate(USER_UPDATE_DATE);

    return user;
  }

}
