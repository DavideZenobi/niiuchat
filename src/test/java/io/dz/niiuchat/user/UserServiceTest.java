package io.dz.niiuchat.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import io.dz.niiuchat.domain.tables.pojos.Users;
import io.dz.niiuchat.user.repository.RoleRepository;
import io.dz.niiuchat.user.repository.UserRepository;
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

  private DSLContext dslContext;
  private UserService userService;

  @BeforeEach
  void init() {
    MockDataProvider provider = mock(MockDataProvider.class);
    MockConnection connection = new MockConnection(provider);
    dslContext = DSL.using(connection, SQLDialect.MARIADB);

    when(userRepository.create(any(), any())).thenReturn(new Users());

    userService = new UserService(userRepository, roleRepository, dslContext, passwordEncoder);
  }

  @Test
  @DisplayName(value = "createUserAndAddRole(...) creates a user and attaches the given role")
  void createUserAndAddRole_1() {
    userService.createUser(new Users());

    verify(userRepository).create(any(), any());
    verify(userRepository).addRole(any(), any(), any());
    verifyNoMoreInteractions(userRepository);
  }

}
