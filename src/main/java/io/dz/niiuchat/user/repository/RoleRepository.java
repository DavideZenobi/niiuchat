package io.dz.niiuchat.user.repository;

import static io.dz.niiuchat.domain.tables.Roles.ROLES;
import static io.dz.niiuchat.domain.tables.UsersRoles.USERS_ROLES;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.jooq.DSLContext;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepository {

  private final DSLContext dslContext;

  public RoleRepository(DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  public List<String> getRoles(Long userId) {
    return dslContext.select(ROLES.ID)
        .from(ROLES)
        /**/.join(USERS_ROLES)
        /*  */.on(USERS_ROLES.ROLE_ID.eq(ROLES.ID))
        .where(USERS_ROLES.USER_ID.eq(userId))
        .fetchInto(String.class);
  }

  public Set<GrantedAuthority> getRolesAsGrantedAuthorities(Long userId) {
    return getRoles(userId).stream()
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toSet());
  }

}
