package de.bitnoise.sonferenz.service.v2.services.idp;

import java.util.List;

import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.service.v2.services.idp.provider.Idp;


public interface IdpService
{
  String findProviderForIdentity(String name);
  
  void createIdentity(String provider, String name, String password);
  
  void setPassword(String provider, String name, String password);
  
  boolean checkIdentity(String provider, String name);
  
  boolean authenticate(String provider, String name, String password);
  
  List<String> getAvailableProviders();
  
  Identity getIdentity(String provider, String name);

  Idp getProviderForUser(UserModel user);
  
}
