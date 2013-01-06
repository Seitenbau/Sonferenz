package de.bitnoise.sonferenz.web.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import de.bitnoise.sonferenz.model.LocalUserModel;
import de.bitnoise.sonferenz.repo.LocalUserRepository;
import de.bitnoise.sonferenz.service.v2.exceptions.RepositoryException;
import de.bitnoise.sonferenz.service.v2.security.ProviderType;
import de.bitnoise.sonferenz.service.v2.services.UserService;
import de.bitnoise.sonferenz.service.v2.services.idp.provider.local.LocalIdp;

public class DatabaseUserDetailsServiceImpl implements UserDetailsService
{
  @Autowired
  UserService userService;
  
  @Autowired
  LocalUserRepository localRepo;
  
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException, DataAccessException
  {
    LocalUserModel localUser = null;
    try
    {
      localUser = localRepo.findByName(username);
    }
    catch (Throwable t)
    {
      throw new RepositoryException(t);
    }
    
    if (localUser == null)
    {
      throw new UsernameNotFoundException("user not found");
    }
    return buildUserFromUserEntity(localUser);
  }

  User buildUserFromUserEntity(LocalUserModel userEntity)
  {
    String username = userEntity.getName();
    String password = userEntity.getPassword();
    boolean enabled = true;
    boolean accountNonExpired = true;
    boolean credentialsNonExpired = true;
    boolean accountNonLocked = true;

    Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

    List<String> roles = userService.getAllUserRoles(username, LocalIdp.IDP_NAME);
//    List<String> roles = userService
//        .getAllRolesForUser(username, PROVIDER_TYPE);
    for (String role : roles)
    {
      authorities.add(new GrantedAuthorityImpl("ROLE_" + role));
    }
    authorities.add(new GrantedAuthorityImpl("ROLE_LOCAL_DB"));

    User user = new DatabaseUserDetails(username, password, enabled,
        accountNonExpired, credentialsNonExpired, accountNonLocked,
        authorities);
    return user;
  }

  public static class DatabaseUserDetails extends User implements ProviderType
  {
    public DatabaseUserDetails(String username, String password,
        boolean enabled, boolean accountNonExpired,
        boolean credentialsNonExpired, boolean accountNonLocked,
        Collection<GrantedAuthority> authorities)
    {
      super(username, password, enabled,
           accountNonExpired, credentialsNonExpired, accountNonLocked,
           authorities);
    }

    public String getProviderType()
    {
      return LocalIdp.IDP_NAME;
    }

  }
}
