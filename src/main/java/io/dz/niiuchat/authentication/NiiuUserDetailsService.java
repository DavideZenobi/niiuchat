package io.dz.niiuchat.authentication;

import io.dz.niiuchat.domain.tables.pojos.Users;
import io.dz.niiuchat.user.repository.RoleRepository;
import io.dz.niiuchat.user.repository.UserRepository;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class NiiuUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  public NiiuUserDetailsService(
      UserRepository userRepository,
      RoleRepository roleRepository
  ) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Users user = userRepository.get(username)
        .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " was not found"));

    Set<GrantedAuthority> roles = roleRepository.getRolesAsGrantedAuthorities(user.getId());

    String password = user.getPassword();
    user.setPassword(null);

    return new NiiuUser(
        user.getUsername(),
        password,
        UsersStatus.ACTIVE.toString().equals(user.getStatus()),
        true,
        true,
        true,
        roles,
        user);
  }

}
