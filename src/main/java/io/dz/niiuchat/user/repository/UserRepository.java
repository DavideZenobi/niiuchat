package io.dz.niiuchat.user.repository;

import static io.dz.niiuchat.domain.tables.Users.USERS;
import static io.dz.niiuchat.domain.tables.UsersRoles.USERS_ROLES;

import io.dz.niiuchat.domain.tables.pojos.Users;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

  private final DSLContext dslContext;

  public UserRepository(DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  public Optional<Users> get(String email) {
    Users user = dslContext.select()
        .from(USERS)
        .where(USERS.EMAIL.eq(email))
        .fetchOneInto(Users.class);

    return Optional.ofNullable(user);
  }

  public Users create(Users user) {
    return create(null, user);
  }

  public Users create(Configuration configuration, Users user) {
    DSLContext currentContext = (configuration != null) ?
        DSL.using(configuration) :
        dslContext;

    Long id = currentContext.insertInto(
        USERS,
        USERS.USERNAME,
        USERS.PASSWORD,
        USERS.EMAIL,
        USERS.STATUS,
        USERS.CREATE_DATE
    ).values(
        user.getUsername(),
        user.getPassword(),
        user.getEmail(),
        user.getStatus(),
        user.getCreateDate()
    )
        .returningResult(USERS.ID)
        .fetchOne().value1();

    user.setId(id);

    return user;
  }

  public List<Users> getAll(Long id) { return getAll(null, id); }

  public List<Users> getAll(Configuration configuration, Long id) {
    DSLContext currentContext = (configuration != null) ?
        DSL.using(configuration) :
        dslContext;

    return currentContext.select()
        .from(USERS)
        .where(USERS.ID.notEqual(id))
        .fetchInto(Users.class);
  }

  public List<Users> getByIds(List<Long> ids) {
    return dslContext.select()
        .from(USERS)
        .where(USERS.ID.in(ids))
        .fetchInto(Users.class);

  }

  public Users updateData(Users user) {
    return updateData(null, user);
  }

  public Users updateData(Configuration configuration, Users user) {
    DSLContext currentContext = (configuration != null) ?
        DSL.using(configuration) :
        dslContext;

    currentContext.update(USERS)
        .set(USERS.USERNAME, user.getUsername())
        .set(USERS.EMAIL, user.getEmail())
        .set(USERS.UPDATE_DATE, user.getUpdateDate())
        .where(USERS.ID.eq(user.getId()))
        .execute();

    return user;
  }

  public void updatePassword(Long id, String password, LocalDateTime updateDate) {
    updatePassword(null, id, password, updateDate);
  }

  public void updatePassword(Configuration configuration, Long id, String password, LocalDateTime updateDate) {
    DSLContext currentContext = (configuration != null) ?
        DSL.using(configuration) :
        dslContext;

    currentContext.update(USERS)
        .set(USERS.PASSWORD, password)
        .set(USERS.UPDATE_DATE, updateDate)
        .where(USERS.ID.eq(id))
        .execute();
  }

  public void updateUserAvatar(Long id, String avatarId, LocalDateTime updateDate) {
    updateUserAvatar(null, id, avatarId, updateDate);
  }

  public void updateUserAvatar(Configuration configuration, Long id, String avatarId, LocalDateTime updateDate) {
    DSLContext currentContext = (configuration != null) ?
        DSL.using(configuration) :
        dslContext;

    currentContext.update(USERS)
        .set(USERS.AVATAR_ID, avatarId)
        .set(USERS.UPDATE_DATE, updateDate)
        .where(USERS.ID.eq(id))
        .execute();
  }

  public void addRole(Long userId, String role) {
    addRole(null, userId, role);
  }

  public void addRole(Configuration configuration, Long userId, String role) {
    DSLContext currentContext = (configuration != null) ?
        DSL.using(configuration) :
        dslContext;

    currentContext.insertInto(
        USERS_ROLES,
        USERS_ROLES.USER_ID,
        USERS_ROLES.ROLE_ID
    ).values(
        userId,
        role
    ).execute();
  }

}
