package io.dz.niiuchat.authentication;

import static io.dz.niiuchat.domain.tables.Users.USERS;

import io.dz.niiuchat.domain.tables.pojos.Users;
import java.util.Optional;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

  private final DSLContext dslContext;

  public UserRepository(DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  public Optional<Users> getUser(String email) {
    Users user = dslContext.select()
        .from(USERS)
        .where(USERS.EMAIL.eq(email))
        .fetchOneInto(Users.class);

    return Optional.ofNullable(user);
  }

}
