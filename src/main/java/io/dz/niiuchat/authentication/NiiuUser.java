package io.dz.niiuchat.authentication;

import io.dz.niiuchat.domain.tables.pojos.Users;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.security.Principal;
import java.util.Collection;
import java.util.Objects;

public class NiiuUser extends User {

  private final Users user;

  public NiiuUser(
          String username,
          String password,
          Collection<? extends GrantedAuthority> authorities,
          Users user
  ) {
    super(username, password, authorities);
    this.user = user;
  }

  public NiiuUser(
          String username,
          String password,
          boolean enabled,
          boolean accountNonExpired,
          boolean credentialsNonExpired,
          boolean accountNonLocked,
          Collection<? extends GrantedAuthority> authorities,
          Users user
  ) {
    super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    this.user = user;
  }

  public static NiiuUser from(Principal principal) {
    return ((NiiuUser) ((UsernamePasswordAuthenticationToken) principal).getPrincipal());
  }

  public static NiiuUser createDefault(String username, String password, String status, Collection<? extends GrantedAuthority> authorities, Users user) {
    return new NiiuUser(
            username,
            password,
            UserStatus.ACTIVE.toString().equals(status),
            true,
            true,
            true,
            authorities,
            user);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    NiiuUser niiuUser = (NiiuUser) o;
    return Objects.equals(user, niiuUser.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), user);
  }

  public Users getUser() {
    return user;
  }

}
