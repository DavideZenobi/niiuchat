package io.dz.niiuchat.user;

import io.dz.niiuchat.domain.tables.pojos.Users;
import io.dz.niiuchat.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.jooq.DSLContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private static final String DEFAULT_PUBLIC_ROLE = "ROLE_USER";

  private final UserRepository userRepository;
  private final DSLContext dslContext;
  private final PasswordEncoder passwordEncoder;

  public UserService(
      UserRepository userRepository,
      DSLContext dslContext,
      PasswordEncoder passwordEncoder
  ) {
    this.userRepository = userRepository;
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

      userRepository.addRole(configuration, createdUser.getId(), DEFAULT_PUBLIC_ROLE);

      return createdUser;
    });
  }

}
