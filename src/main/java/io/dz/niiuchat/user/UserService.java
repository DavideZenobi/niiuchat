package io.dz.niiuchat.user;

import io.dz.niiuchat.authentication.NiiuUser;
import io.dz.niiuchat.authentication.UserRole;
import io.dz.niiuchat.authentication.UserStatus;
import io.dz.niiuchat.common.ImageService;
import io.dz.niiuchat.domain.tables.pojos.Files;
import io.dz.niiuchat.domain.tables.pojos.Users;
import io.dz.niiuchat.user.repository.RoleRepository;
import io.dz.niiuchat.user.repository.UserRepository;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;
import org.apache.tika.mime.MediaType;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

  private final ImageService imageService;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final DSLContext dslContext;
  private final PasswordEncoder passwordEncoder;

  public UserService(
      ImageService imageService,
      UserRepository userRepository,
      RoleRepository roleRepository,
      DSLContext dslContext,
      PasswordEncoder passwordEncoder
  ) {
    this.imageService = imageService;
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

  public void upsertAvatar(InputStream inputStream, Long userId) {
    try {
      MediaType avatarMediaType = imageService.getMediaType(inputStream);

      if (!imageService.isImage(avatarMediaType)) {
        throw new RuntimeException("File is not image");
      }

      BufferedImage avatar = ImageIO.read(inputStream);
      BufferedImage resizedAvatar = imageService.resizeAvatar(avatar);
      String imageName = imageService.saveAvatar(resizedAvatar, avatarMediaType.getSubtype(), userId);

      Files file = new Files();
      file.setMediaType(avatarMediaType.toString());
      file.setName(imageName);
      file.setPath(Paths.get("niiu", "avatars", imageName).toString());
      file.setType("AVATAR");
      file.setCreateDate(LocalDateTime.now(ZoneOffset.UTC));

      LOGGER.info("Saved image {}", file.getPath());
    } catch (IOException e) {
      LOGGER.error("Error processing Avatar image", e);
    }
  }

}
