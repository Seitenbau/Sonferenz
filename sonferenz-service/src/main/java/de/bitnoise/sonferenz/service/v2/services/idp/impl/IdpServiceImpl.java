package de.bitnoise.sonferenz.service.v2.services.idp.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.service.v2.services.idp.Identity;
import de.bitnoise.sonferenz.service.v2.services.idp.IdpService;
import de.bitnoise.sonferenz.service.v2.services.idp.provider.Idp;

@Service
public class IdpServiceImpl implements IdpService
{

  private List<Idp> idpList;

  @Autowired
  public void setIdpList(List<Idp> idpList)
  {
    this.idpList = idpList;
  }

  private Idp getIdp(String providerName)
  {
    for (Idp idp : idpList)
    {
      if (idp.getProviderName().equals(providerName))
      {
        return idp;
      }
    }
    return null;
  }

  @Override
  public void createIdentity(String provider, String name, String password)
  {
    Idp idp = getIdp(provider);
    idp.createIdentity(name, password);
  }

  @Override
  public void setPassword(String provider, String name, String password)
  {
    Idp idp = getIdp(provider);
    idp.setPassword(name, password);
  }

  @Override
  public String findProviderForIdentity(String name)
  {
    for (Idp idp : idpList)
    {
      boolean exist = idp.checkIdentityExist(name);
      if (exist)
      {
        return idp.getProviderName();
      }
    }
    return null;
  }
  
  @Override
  public Idp getProviderForUser(UserModel user)
  {
	  String type = user.getProvider().getAuthType();
	  return getIdp(type);
  }

  @Override
  public List<String> getAvailableProviders()
  {
    List<String> providersList = new ArrayList<String>();
    for (Idp idp : idpList)
    {
      providersList.add(idp.getProviderName());
    }
    return providersList;
  }

  @Override
  public boolean authenticate(String provider, String name, String password)
  {
    Idp idp = getIdp(provider);
    return idp.authenticate(name, password);
  }

  @Override
  public boolean checkIdentity(String provider, String name)
  {
    Idp idp = getIdp(provider);
    return idp.checkIdentityExist(name);
  }

  @Override
  public Identity getIdentity(String provider, String name)
  {
    Idp idp = getIdp(provider);
    return idp.getIdentity(name);
  }

}
