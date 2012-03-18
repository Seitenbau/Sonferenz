package de.bitnoise.sonferenz.service.v2.services;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import de.bitnoise.sonferenz.model.LocalUserModel;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.model.UserRole;
import de.bitnoise.sonferenz.model.UserRoles;
import de.bitnoise.sonferenz.service.v2.services.idp.Identity;

public interface UserService
{
  UserModel createIdentity(String provider, String username, String password,String email,
      Collection<UserRoles> newRoles);

  List<String> getAllUserRoles(String username, String providerType);

  UserModel getUser(String username, String providerType);

  void createNewUser(UserModel neu, String authId,String authType);

  int getCount();

  Page<UserModel> getAllUsers(PageRequest request);

  List<UserModel> getAllUsers();

  void saveUser(UserModel user, Collection<UserRoles> newRoles);

  void updateUser(UserModel user, String newName);

  boolean checkMailNotExists(String mail);

  Page<UserRole> getAllRoles(PageRequest request);

  boolean checkUserNotExists(String username);
}
