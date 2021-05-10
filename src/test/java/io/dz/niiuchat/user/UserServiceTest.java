package io.dz.niiuchat.user;

import static io.dz.niiuchat.Vars.PNG_RESOURCE_PATH;
import static io.dz.niiuchat.user.UserTestUtil.USER_EMAIL;
import static io.dz.niiuchat.user.UserTestUtil.USER_ENCRYPTED_PASSWORD;
import static io.dz.niiuchat.user.UserTestUtil.USER_ID;
import static io.dz.niiuchat.user.UserTestUtil.USER_PLAIN_PASSWORD;
import static io.dz.niiuchat.user.UserTestUtil.dummyUser;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import io.dz.niiuchat.authentication.UserRole;
import io.dz.niiuchat.common.ImageService;
import io.dz.niiuchat.domain.tables.pojos.Files;
import io.dz.niiuchat.domain.tables.pojos.Users;
import io.dz.niiuchat.storage.dto.ImagePaths;
import io.dz.niiuchat.storage.repository.FileRepository;
import io.dz.niiuchat.storage.service.FileService;
import io.dz.niiuchat.storage.service.StorageService;
import io.dz.niiuchat.user.repository.RoleRepository;
import io.dz.niiuchat.user.repository.UserRepository;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Optional;
import org.apache.tika.mime.MediaType;
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
  private ImageService imageService;

  @Mock
  private StorageService storageService;

  @Mock
  private FileService fileService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private RoleRepository roleRepository;

  @Mock
  private FileRepository fileRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  private UserService userService;

  @BeforeEach
  void init() throws IOException {
    Files dummyAvatar = new Files();
    dummyAvatar.setId("mockId");
    dummyAvatar.setPath("mock/path");

    lenient().when(imageService.getMediaType(any(InputStream.class))).thenReturn(MediaType.image("png"));
    lenient().when(imageService.isAcceptedImage(any(MediaType.class))).thenReturn(true);
    lenient().when(imageService.resizeAvatar(any(BufferedImage.class))).thenReturn(new BufferedImage(1, 1, TYPE_INT_RGB));
    lenient().when(storageService.getAvatarPaths(anyString(), anyLong())).thenReturn(new ImagePaths("mock/path", "/absolute/mock/path"));
    lenient().when(fileService.createAvatar(anyLong(), anyString(), anyString())).thenReturn(dummyAvatar);
    lenient().when(fileRepository.findOne(any(Configuration.class), anyString())).thenReturn(Optional.empty());

    MockDataProvider provider = mock(MockDataProvider.class);
    MockConnection connection = new MockConnection(provider);
    DSLContext dsl = DSL.using(connection, SQLDialect.MYSQL);

    userService = new UserService(imageService, storageService, fileService, userRepository, roleRepository, fileRepository, dsl, passwordEncoder);
  }

  @Test
  @DisplayName("Create a user and its roles")
  void createUserAndRoles() {
    when(passwordEncoder.encode(anyString())).thenReturn(USER_ENCRYPTED_PASSWORD);
    when(userRepository.create(any(Configuration.class), any(Users.class))).thenReturn(
        dummyUser(false));

    userService.createUser(dummyUser());

    verify(passwordEncoder).encode(USER_PLAIN_PASSWORD);
    verifyNoMoreInteractions(passwordEncoder);

    verify(userRepository).create(any(Configuration.class), any(Users.class));
    verify(userRepository).addRole(any(Configuration.class), eq(USER_ID), eq(UserRole.ROLE_USER.toString()));
    verifyNoMoreInteractions(userRepository);
  }

  /*
  @Test
  @DisplayName("Retrieve all users")
  void retrieveAllUsers() {
    userService.getAll();

    verify(userRepository).getAll();
    verifyNoMoreInteractions(userRepository);
  }*/

  @Test
  @DisplayName("Update user data")
  void updateUserData() {
    when(userRepository.get(any())).thenReturn(Optional.of(dummyUser(false)));

    userService.updateData(dummyUser());

    verify(userRepository).updateData(any(Users.class));
    verify(userRepository).get(USER_EMAIL);
    verifyNoMoreInteractions(userRepository);

    verify(roleRepository).getRolesAsGrantedAuthorities(USER_ID);
    verifyNoMoreInteractions(roleRepository);
  }

  @Test
  @DisplayName("Update user password")
  void updateUserPassword() {
    when(passwordEncoder.encode(anyString())).thenReturn(USER_ENCRYPTED_PASSWORD);

    userService.updatePassword(USER_ID, USER_PLAIN_PASSWORD);

    verify(passwordEncoder).encode(USER_PLAIN_PASSWORD);
    verifyNoMoreInteractions(passwordEncoder);

    verify(userRepository).updatePassword(eq(USER_ID), eq(USER_ENCRYPTED_PASSWORD), any(LocalDateTime.class));
    verifyNoMoreInteractions(userRepository);
  }

  /*@Test
  @DisplayName("Checks that image is accepted when upserting the avatar")
  void upsertAvatarChecksAcceptedImage() throws IOException {
    InputStream imageStream = this.getClass().getClassLoader().getResourceAsStream(PNG_RESOURCE_PATH);
    assert imageStream != null;

    userService.upsertAvatar(any(InputStream.class), 1L);

    verify(imageService).isAcceptedImage(MediaType.image("png"));

    imageStream.close();
  }*/

  @Test
  @DisplayName("Create a new avatar and updates the user")
  void createNewAvatar() throws IOException {
    InputStream imageStream = this.getClass().getClassLoader().getResourceAsStream(PNG_RESOURCE_PATH);
    assert imageStream != null;

    userService.upsertAvatar(imageStream, 1L);

    verify(fileService).createAvatar(anyLong(), anyString(), anyString());
    verify(fileRepository).save(any(Configuration.class), any(Files.class));
    verify(userRepository).updateUserAvatar(any(Configuration.class), anyLong(), anyString(), any(LocalDateTime.class));
    verify(storageService).saveAvatar(any(BufferedImage.class), anyString(), any(ImagePaths.class));

    imageStream.close();
  }

  @Test
  @DisplayName("Tries to delete avatar when an old one is found")
  void deleteOldAvatar() throws IOException {
    Files dummyFile = new Files();
    dummyFile.setPath("mock/path");

    when(fileRepository.findOne(any(Configuration.class), anyString())).thenReturn(Optional.of(dummyFile));

    InputStream imageStream = this.getClass().getClassLoader().getResourceAsStream(PNG_RESOURCE_PATH);
    assert imageStream != null;

    userService.upsertAvatar(imageStream, 1L);

    verify(fileRepository).findOne(any(Configuration.class), anyString());
    verify(storageService).deleteAvatar(anyString());

    imageStream.close();
  }

  @Test
  @DisplayName("Don't tries to delete avatar when an old one is NOT found")
  void dontDeleteOldAvatar() throws IOException {
    InputStream imageStream = this.getClass().getClassLoader().getResourceAsStream(PNG_RESOURCE_PATH);
    assert imageStream != null;

    userService.upsertAvatar(imageStream, 1L);

    verify(fileRepository).findOne(any(Configuration.class), anyString());
    verify(storageService, never()).deleteAvatar(anyString());

    imageStream.close();
  }

}
