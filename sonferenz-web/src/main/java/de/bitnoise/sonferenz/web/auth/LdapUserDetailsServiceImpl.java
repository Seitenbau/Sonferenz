package de.bitnoise.sonferenz.web.auth;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;

import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.service.v2.services.AuthenticationService;
import de.bitnoise.sonferenz.service.v2.services.UserService;

public class LdapUserDetailsServiceImpl extends DefaultLdapAuthoritiesPopulator
{
  Logger logger = LoggerFactory.getLogger(LdapUserDetailsServiceImpl.class);

  public LdapUserDetailsServiceImpl(ContextSource contextSource,
      String groupSearchBase)
  {
    super(contextSource, groupSearchBase);
  }

  @Autowired
  AuthenticationService authService;

  @Autowired
  UserService userService;

  @Override
  protected Set<GrantedAuthority> getAdditionalRoles(DirContextOperations user,
      String username)
  {
    logger.debug("getAdditionalRoles() : begin");
    Set<GrantedAuthority> result = new HashSet<GrantedAuthority>();

    UserModel dbUser2 = userService.getUser(username, "ldap");
    if (dbUser2 == null)
    {
      UserModel neu = new UserModel();
      neu.setName(username);
      neu.setEmail(getEmailFomrLDAP(user));
      neu.setName(username);
      neu.setName(username);
      userService.createNewUser(neu, username, "ldap");
    }
    else
    {
      // List<String> roles2 = userService.getAllUserRoles(username, "ldap");
      //
      // UserModel dbUser = authService.getUserFor(username, "ldap");
      // if (dbUser == null)
      // {
      // logger.debug("creating new user " + username);
      // // create new user
      // UserModel neu = userService.createMappedUser(username, username,
      // "ldap",
      // UserRoles.USER);
      // try
      // {
      // Attributes mailAttibs = user.getAttributes("", new String[] {"mail"});
      // if (mailAttibs != null && mailAttibs.get("mail") != null)
      // {
      // Object value = mailAttibs.get("mail").get();
      // if (value instanceof String)
      // {
      // neu.setEmail((String) value);
      // userService.save(neu);
      // }
      // }
      // }
      // catch (NamingException e)
      // {
      // Logger.getLogger(LdapUserDetailsServiceImpl.class).error(e);
      // }
      // }
      // else
      // {
      logger.debug("enrichting user " + username);
      // enrich user
      List<String> roles = userService.getAllUserRoles(username, "ldap");
      for (String role : roles)
      {
        logger.debug("enrichting user with " + role);
        result.add(new GrantedAuthorityImpl("ROLE_" + role));
      }
      logger.debug("enrichting user with ROLE_LDAP");
      result.add(new GrantedAuthorityImpl("ROLE_LDAP"));
    }
    logger.debug("getAdditionalRoles() : end");
    return result;
  }

  private String getEmailFomrLDAP(DirContextOperations user)
  {
    try
    {
      Attributes mailAttibs = user.getAttributes("", new String[]
        { "mail" });
      if (mailAttibs != null && mailAttibs.get("mail") != null)
      {
        Object value = mailAttibs.get("mail").get();
        if (value instanceof String)
        {
          return (String) value;
        }
      }
    }
    catch (NamingException e)
    {
      LoggerFactory.getLogger(LdapUserDetailsServiceImpl.class).error("",e);
    }
    return null;
  }
}
