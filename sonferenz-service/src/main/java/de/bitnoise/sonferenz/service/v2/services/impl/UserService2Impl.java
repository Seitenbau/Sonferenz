package de.bitnoise.sonferenz.service.v2.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jasypt.digest.StringDigester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.bitnoise.sonferenz.model.AuthMapping;
import de.bitnoise.sonferenz.model.LocalUserModel;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.model.UserRole;
import de.bitnoise.sonferenz.model.UserRoles;
import de.bitnoise.sonferenz.repo.AuthmappingRepository;
import de.bitnoise.sonferenz.repo.LocalUserRepository;
import de.bitnoise.sonferenz.repo.RoleRepository;
import de.bitnoise.sonferenz.repo.UserRepository;
import de.bitnoise.sonferenz.service.v2.Detach;
import de.bitnoise.sonferenz.service.v2.exceptions.RepositoryException;
import de.bitnoise.sonferenz.service.v2.services.UserService;
import de.bitnoise.sonferenz.service.v2.services.idp.Identity;
import de.bitnoise.sonferenz.service.v2.services.idp.IdpService;

@Service
public class UserService2Impl implements UserService
{
  @Autowired
  LocalUserRepository localRepo;

  @Autowired
  UserRepository userRepo;

  @Autowired
  AuthmappingRepository authRepo;

  @Autowired
  RoleRepository roleRepo;
  
  @Autowired
  IdpService idpService;

  @Override
  @Transactional(readOnly = true)
  public List<String> getAllUserRoles(String authId, String authType)
  {
    try
    {
      UserModel user = getUser(authId, authType);
      if (user == null)
      {
        throw new RepositoryException("Benutzer existiert nicht");
      }
      Set<UserRole> roles = user.getRoles();
      if (roles == null || roles.isEmpty())
      {
        return new ArrayList<String>();
      }

      List<String> result = new ArrayList<String>();
      for (UserRole role : roles)
      {
        result.add(role.getName());
      }
      return result;
    }
    catch (Throwable t)
    {
      throw new RepositoryException(t);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public UserModel getUser(String authId, String authType)
  {
    AuthMapping auth = authRepo.findByAuthIdAndAuthType(authId, authType);
    if (auth == null)
    {
      return null;
    }
    UserModel user = auth.getUser();
    return user;
  }

  @Override
  @Transactional
  public void createNewUser(UserModel neu, String authId, String authType)
  {
    userRepo.save(neu);
    AuthMapping auth = authRepo.findByAuthIdAndAuthType(authId, authType);
    if (auth == null)
    {
      auth = new AuthMapping();
      auth.setAuthId(authId);
      auth.setAuthType(authType);
      auth.setUser(neu);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public int getCount()
  {
    return (int) userRepo.count();
  }

  @Override
  @Transactional(readOnly = true)
  public Page<UserModel> getAllUsers(PageRequest request)
  {
    return Detach.detach(userRepo.findAll(request));
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserModel> getAllUsers()
  {
    return Detach.detach(userRepo.findAll());
  }

  private StringDigester _digester;

  @Override
  @Transactional
  public UserModel createIdentity(String provider, String username, String password,String email,
      Collection<UserRoles> newRoles)
  {
    idpService.createIdentity(provider, username, password);
    
    AuthMapping auth = new AuthMapping();
    auth.setAuthId(username);
    auth.setAuthType(provider);
    authRepo.save(auth);

    UserModel user = new UserModel();
    user.setName(username);
    user.setProvider(auth);
    user.setEmail(email);
    user.setRoles(new HashSet<UserRole>());
    setupRoles(user, newRoles);
    userRepo.save(user);

    auth.setUser(user);
    authRepo.save(auth);
    
    return user;
  }

  private void setupRoles(UserModel user, Collection<UserRoles> newRoles)
  {
    user.getRoles().clear();
    for (UserRoles role : newRoles)
    {
      if (role.equals(UserRoles.NONE))
      {
        user.getRoles().clear();
        break;
      }
      UserRole dbRole = getRoleFor(role);
      user.getRoles().add(dbRole);
    }
  }

  public UserRole getRoleFor(UserRoles role)
  {
    UserRole dbRole = roleRepo.findByName(role.toString());
    if (dbRole == null)
    {
      return null;
    }
    return dbRole;
  }

  private String sign(String password)
  {
    if (_digester == null)
    {
      return password;
    }
    String result = _digester.digest(password);
    return result;
  }

  @Autowired(required = false)
  public void setDigester(StringDigester digester)
  {
    _digester = digester;
  }

  @Override
  @Transactional
  public void saveUser(UserModel user, Collection<UserRoles> newRoles)
  {
    setupRoles(user, newRoles);
    userRepo.save(user);
  }

  @Override
  @Transactional
  public void updateUser(UserModel user, String newName)
  {
    UserModel current = userRepo.findOne(user.getId());
    current.setName(newName);
    userRepo.save(current);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean checkMailNotExists(String mail)
  {
    UserModel result = userRepo.findByEmail(mail);
    return result == null;
  }

  @Override
  public Page<UserRole> getAllRoles(PageRequest request)
  {
    return roleRepo.findAll(request);
  }

  @Override
  public boolean checkUserNotExists(String username) {
    UserModel result = userRepo.findByName(username);
    return result == null;
  }

}
