package de.bitnoise.sonferenz.service.v2.services.idp.provider;

import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.service.v2.services.idp.Identity;


public interface Idp
{
  boolean checkIdentityExist(String name);
  
  void createIdentity(String name, String password);
  
  void setPassword(String name, String password);
  
  String getProviderName();
  
  Identity getIdentity(String name);

  boolean authenticate(String name, String password);

  boolean supportsPasswordChange();

  void setUserPassword(UserModel user, String newPassword);
}
