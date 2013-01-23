package de.bitnoise.sonferenz.service.v2.services.idp.provider.local;

import org.jasypt.digest.StringDigester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.bitnoise.sonferenz.model.LocalUserModel;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.repo.LocalUserRepository;
import de.bitnoise.sonferenz.service.v2.exceptions.RepositoryException;
import de.bitnoise.sonferenz.service.v2.monitor.IMonitorState;
import de.bitnoise.sonferenz.service.v2.monitor.IMonitorable;
import de.bitnoise.sonferenz.service.v2.monitor.MonitorState;
import de.bitnoise.sonferenz.service.v2.monitor.MonitorStateEnum;
import de.bitnoise.sonferenz.service.v2.services.idp.Identity;
import de.bitnoise.sonferenz.service.v2.services.idp.provider.Idp;

@Component
public class LocalIdp implements Idp, IMonitorable
{

  public static String IDP_NAME = "plainDB";

  @Autowired
  LocalUserRepository localRepo;

  @Autowired(required = false)
  StringDigester _digester;

  @Override
  public String getProviderName()
  {
    return IDP_NAME;
  }

  private LocalUserModel map(String name, String password)
  {
    LocalUserModel model = new LocalUserModel();
    model.setName(name);
    String pwd = sign(password);
    model.setPassword(pwd);
    return model;
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

  @Override
  @Transactional(readOnly = true)
  public boolean checkIdentityExist(String name)
  {
    try
    {
      LocalUserModel u = localRepo.findByName(name);
      if (u != null)
      {
        return true;
      }
      return false;
    } catch (Throwable t)
    {
      throw new RepositoryException(t);
    }
  }

  @Override
  @Transactional
  public void createIdentity(String name, String password)
  {
    try
    {
      localRepo.save(map(name, password));
    } catch (Throwable t)
    {
      throw new RepositoryException(t);
    }
  }

  @Override
  @Transactional
  public void setPassword(String name, String password)
  {
    try
    {
      localRepo.save(map(name, password));
    } catch (Throwable t)
    {
      throw new RepositoryException(t);
    }
  }

  @Override
  public boolean authenticate(String name, String password)
  {
    return false;
  }

  @Override
  public Identity getIdentity(String name)
  {
    return null;
  }

  @Override
  public boolean supportsPasswordChange() {
    return true;
  }

  @Override
  @Transactional
  public void setUserPassword(UserModel user, String newPassword) {
    if (!user.getProvider().getAuthType().equals(IDP_NAME)) {
      throw new RepositoryException("Provider dosn't match for this user");
    }
    try
    {
      LocalUserModel dbUser = localRepo.findByName(user.getProvider().getAuthId());
      dbUser.setPassword(sign(newPassword));
      localRepo.save(dbUser);
    } catch (Throwable t)
    {
      throw new RepositoryException(t);
    }
  }

  @Override
  public IMonitorState getState() {
    return new MonitorState("IDP", "IDP.Provider.LocalDatabase", MonitorStateEnum.OK);
  }

}
