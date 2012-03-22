package de.bitnoise.sonferenz.service.v2.services.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.bitnoise.sonferenz.model.AuthMapping;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.model.UserRole;
import de.bitnoise.sonferenz.model.UserRoles;
import de.bitnoise.sonferenz.repo.AuthmappingRepository;
import de.bitnoise.sonferenz.repo.RoleRepository;
import de.bitnoise.sonferenz.repo.UserRepository;
import de.bitnoise.sonferenz.service.v2.Detach;
import de.bitnoise.sonferenz.service.v2.exceptions.GeneralConferenceException;
import de.bitnoise.sonferenz.service.v2.security.ProviderType;
import de.bitnoise.sonferenz.service.v2.security.ProvidesEmail;
import de.bitnoise.sonferenz.service.v2.services.AuthenticationService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService
{

  @Autowired
  AuthmappingRepository authRepo;
  
  @Autowired
  UserRepository userRepo;

  @Autowired
  RoleRepository rolesRepo;

  @Override
  @Transactional
  public UserModel getCurrentUser() throws GeneralConferenceException
  {
    try
    {
      return internGetCurrentUser();
    }
    catch (Throwable t)
    {
      throw new GeneralConferenceException("Unerwarteter Fehler aufgetreten",t);
    }
  }

  private UserModel internGetCurrentUser()
  {
    SecurityContext context = SecurityContextHolder.getContext();
    if (context == null)
    {
      return null;
    }
    Thread t = Thread.currentThread();
    Authentication authentication = context.getAuthentication();
    if (authentication == null)
    {
      return null;
    }
    String providerId = authentication.getName();
    String providerType = null;
    Object principal = authentication.getPrincipal();
    if (principal instanceof LdapUserDetails)
    {
      providerType = "ldap";
    }
    if (authentication instanceof ProviderType)
    {
      providerType = ((ProviderType) authentication).getProviderType();
    }
    if (principal instanceof ProviderType)
    {
      providerType = ((ProviderType) principal)
          .getProviderType();
    }
    if (providerType == null)
    {
      return null;
    }
    AuthMapping mapping = authRepo.findByAuthIdAndAuthType(providerId,
        providerType);
    if (mapping == null)
    {
      String email = null;
      if (principal instanceof ProvidesEmail)
      {
        ProvidesEmail  user = (ProvidesEmail) principal;
        email = user.getEmail();
      }
      UserModel neuerUser = neuenNutzerAnlegen(providerId, email);
      neuesAuthMappingAnlegen(neuerUser, providerId, providerType);
      return neuerUser;
    }
    return Detach.detach(mapping.getUser());
  }

  private void neuesAuthMappingAnlegen(UserModel neuerUser, String providerId,
      String providerType)
  {
    AuthMapping neuesMapping = new AuthMapping();
    neuesMapping.setAuthId(providerId);
    neuesMapping.setAuthType(providerType);
    neuesMapping.setUser(neuerUser);
    authRepo.save(neuesMapping);
  }

  private UserModel neuenNutzerAnlegen(String providerId, String email)
  {
    UserModel neuerUser = new UserModel();
    neuerUser.setName(providerId);
    neuerUser.setEmail(email);
    Set<UserRole> newRoles = new HashSet<UserRole>();
	
    UserRole userRole = rolesRepo.findByName(UserRoles.USER.toString());
    Set<UserRole> roles=new HashSet<UserRole>();
    roles.add(userRole);
    neuerUser.setRoles(roles);
    
	neuerUser.setRoles(newRoles);
    userRepo.save(neuerUser);
    return neuerUser;
  }
}
