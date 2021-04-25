package io.dz.niiuchat.user;

import io.dz.niiuchat.authentication.NiiuUser;
import io.dz.niiuchat.authentication.UserRole;
import io.dz.niiuchat.authentication.UserStatus;
import io.dz.niiuchat.domain.tables.pojos.Users;
import io.dz.niiuchat.user.repository.RoleRepository;
import io.dz.niiuchat.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import org.jooq.DSLContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final DSLContext dslContext;
  private final PasswordEncoder passwordEncoder;

  public UserService(
      UserRepository userRepository,
      RoleRepository roleRepository,
      DSLContext dslContext,
      PasswordEncoder passwordEncoder
  ) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.dslContext = dslContext;
    this.passwordEncoder = passwordEncoder;
  }

  public Users createUser(Users user) {
    Users userToCreate = new Users();
    userToCreate.setUsername(user.getUsername());
    userToCreate.setEmail(user.getEmail());
    userToCreate.setPassword(passwordEncoder.encode(user.getPassword()));
    userToCreate.setStatus(user.getStatus());
    userToCreate.setCreateDate(LocalDateTime.now(ZoneOffset.UTC));

    return dslContext.transactionResult(configuration -> {
      Users createdUser = userRepository.create(configuration, userToCreate);

      userRepository.addRole(configuration, createdUser.getId(), UserRole.ROLE_USER.toString());

      return createdUser;
    });
  }

  public List<Users> getAll() {
    return userRepository.getAll();
  }

  public void updateData(Users user) {
    user.setUpdateDate(LocalDateTime.now(ZoneOffset.UTC));

    // Update user data
    userRepository.updateData(user);

    // Retrieve full updated user
    Users updatedUser = userRepository.get(user.getEmail())
        .orElseThrow();

    Set<GrantedAuthority> roles = roleRepository.getRolesAsGrantedAuthorities(updatedUser.getId());

    String password = updatedUser.getPassword();
    updatedUser.setPassword(null);

    NiiuUser niiuUser = new NiiuUser(
        updatedUser.getUsername(),
        password,
        UserStatus.ACTIVE.toString().equals(updatedUser.getStatus()),
        true,
        true,
        true,
        roles,
        updatedUser);

    Authentication authentication = new UsernamePasswordAuthenticationToken(niiuUser, password, roles);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  public void updatePassword(Long id, String password) {
    userRepository.updatePassword(id, passwordEncoder.encode(password), LocalDateTime.now(ZoneOffset.UTC));
  }

}
