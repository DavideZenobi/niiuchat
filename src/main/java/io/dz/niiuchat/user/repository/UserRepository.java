package io.dz.niiuchat.user.repository;

import static io.dz.niiuchat.domain.tables.Users.USERS;
import static io.dz.niiuchat.domain.tables.UsersRoles.USERS_ROLES;

import io.dz.niiuchat.domain.tables.pojos.Users;
import java.util.Optional;
import java.util.List;
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

  public List<Users> getAll(Configuration configuration) {
    DSLContext currentContext = (configuration != null) ?
        DSL.using(configuration) :
        dslContext;

    return currentContext.select().from(USERS).fetchInto(Users.class);
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
