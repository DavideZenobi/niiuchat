package io.dz.niiuchat.user;

import static io.dz.niiuchat.user.UserTestUtil.USER_EMAIL;
import static io.dz.niiuchat.user.UserTestUtil.USER_ENCRYPTED_PASSWORD;
import static io.dz.niiuchat.user.UserTestUtil.USER_ID;
import static io.dz.niiuchat.user.UserTestUtil.USER_PLAIN_PASSWORD;
import static io.dz.niiuchat.user.UserTestUtil.stubbedUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import io.dz.niiuchat.domain.tables.pojos.Users;
import io.dz.niiuchat.user.repository.RoleRepository;
import io.dz.niiuchat.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.tools.jdbc.MockConnection;
import org.jooq.tools.jdbc.MockDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private RoleRepository roleRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  private UserService userService;

  @BeforeEach
  void init() {
    MockDataProvider provider = mock(MockDataProvider.class);
    MockConnection connection = new MockConnection(provider);
    DSLContext dsl = DSL.using(connection, SQLDialect.MARIADB);

    userService = new UserService(userRepository, roleRepository, dsl, passwordEncoder);
  }

  @Test
  @DisplayName(value = "Create a user and its roles")
  void createUserAndRoles() {
    when(passwordEncoder.encode(anyString())).thenReturn(USER_ENCRYPTED_PASSWORD);
    when(userRepository.create(any(Configuration.class), any(Users.class))).thenReturn(stubbedUser(false));

    userService.createUser(stubbedUser());

    verify(passwordEncoder).encode(USER_PLAIN_PASSWORD);
    verifyNoMoreInteractions(passwordEncoder);

    verify(userRepository).create(any(Configuration.class), any(Users.class));
    verify(userRepository).addRole(any(Configuration.class), eq(USER_ID), eq(UserService.DEFAULT_PUBLIC_ROLE));
    verifyNoMoreInteractions(userRepository);
  }

  @Test
  @DisplayName(value = "Retrieve all users")
  void retrieveAllUsers() {
    userService.getAll();

    verify(userRepository).getAll();
    verifyNoMoreInteractions(userRepository);
  }

  @Test
  @DisplayName(value = "Update user data")
  void updateUserData() {
    when(userRepository.get(any())).thenReturn(Optional.of(stubbedUser(false)));

    userService.updateData(stubbedUser());

    verify(userRepository).updateData(any(Users.class));
    verify(userRepository).get(USER_EMAIL);
    verifyNoMoreInteractions(userRepository);

    verify(roleRepository).getRolesAsGrantedAuthorities(USER_ID);
    verifyNoMoreInteractions(roleRepository);
  }

  @Test
  @DisplayName(value = "Update user password")
  void updateUserPassword() {
    when(passwordEncoder.encode(anyString())).thenReturn(USER_ENCRYPTED_PASSWORD);

    userService.updatePassword(USER_ID, USER_PLAIN_PASSWORD);

    verify(passwordEncoder).encode(USER_PLAIN_PASSWORD);
    verifyNoMoreInteractions(passwordEncoder);

    verify(userRepository).updatePassword(eq(USER_ID), eq(USER_ENCRYPTED_PASSWORD), any(LocalDateTime.class));
    verifyNoMoreInteractions(userRepository);
  }

}
