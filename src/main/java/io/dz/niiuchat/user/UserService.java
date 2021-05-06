package io.dz.niiuchat.user;

import io.dz.niiuchat.authentication.NiiuUser;
import io.dz.niiuchat.authentication.UserRole;
import io.dz.niiuchat.authentication.UserStatus;
import io.dz.niiuchat.common.ImageService;
import io.dz.niiuchat.domain.tables.pojos.Files;
import io.dz.niiuchat.domain.tables.pojos.Users;
import io.dz.niiuchat.storage.dto.ImagePaths;
import io.dz.niiuchat.storage.repository.FileRepository;
import io.dz.niiuchat.storage.service.FileService;
import io.dz.niiuchat.storage.service.StorageService;
import io.dz.niiuchat.user.dto.AvatarDto;
import io.dz.niiuchat.user.repository.RoleRepository;
import io.dz.niiuchat.user.repository.UserRepository;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
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
  private final StorageService storageService;
  private final FileService fileService;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final FileRepository fileRepository;
  private final DSLContext dslContext;
  private final PasswordEncoder passwordEncoder;

  public UserService(
      ImageService imageService,
      StorageService storageService,
      FileService fileService,
      UserRepository userRepository,
      RoleRepository roleRepository,
      FileRepository fileRepository,
      DSLContext dslContext,
      PasswordEncoder passwordEncoder
  ) {
    this.imageService = imageService;
    this.storageService = storageService;
    this.fileService = fileService;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.fileRepository = fileRepository;
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

    NiiuUser niiuUser = NiiuUser.createDefault(
        updatedUser.getUsername(),
        password,
        updatedUser.getStatus(),
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

      if (!imageService.isAcceptedImage(avatarMediaType)) {
        throw new RuntimeException("File is not image");
      }

      BufferedImage avatarImage = ImageIO.read(inputStream);
      BufferedImage resizedAvatarImage = imageService.resizeAvatar(avatarImage);
      ImagePaths avatarPaths = storageService.getAvatarPaths(avatarMediaType.getSubtype(), userId);

      Files avatarFileToSave = fileService.createAvatar(userId, avatarMediaType.toString(), avatarPaths.getRelativePath());

      dslContext.transaction(configuration -> {
        // Get old avatar and delete if exists
        fileRepository.findOne(configuration, avatarFileToSave.getId())
            .ifPresent(oldAvatar -> storageService.deleteAvatar(oldAvatar.getPath()));

        // Update avatar data on storage
        fileRepository.save(configuration, avatarFileToSave);

        // Update user with the updated avatar id and update date
        userRepository.updateUserAvatar(configuration, userId, avatarFileToSave.getId(), LocalDateTime.now(ZoneOffset.UTC));

        // Finally store the new avatar image in the storage
        storageService.saveAvatar(resizedAvatarImage, avatarMediaType.getSubtype(), avatarPaths);

        LOGGER.info("Saved avatar {}", avatarPaths.getRelativePath());
      });
    } catch (IOException e) {
      LOGGER.error("Error processing Avatar image", e);
    }
  }

  public Optional<AvatarDto> findAvatar(long userId) {
    var fileOptional = fileRepository.findOne(storageService.getAvatarId(userId));

    if (fileOptional.isEmpty()) {
      return Optional.empty();
    }

    Files fileRecord = fileOptional.get();
    File imageFile = storageService.getFileFromRelativePath(fileRecord.getPath());

    if (!imageFile.exists()) {
      throw new MissingImageException(imageFile.getAbsolutePath());
    }

    return Optional.of(new AvatarDto(imageFile, fileRecord.getMediaType()));
  }

}
