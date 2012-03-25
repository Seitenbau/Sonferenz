package de.bitnoise.sonferenz.service.v2.services.idp.provider.crowd;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.bitnoise.sonferenz.service.v2.events.ConfigReload;

@Ignore
public class CrowdIdpTest
{

  CrowdIdp target;

  @Before
  public void setUp() throws Exception
  {
    target = new CrowdIdp();
    target.onConfigReload(new ConfigReload());
  }

  @Test
  public void checkIdentityExist_True()
  {
    boolean exist = target.checkIdentityExist("peter");
    Assert.assertTrue(exist);
  }

  @Test
  public void checkIdentityExist_False()
  {
    boolean exist = target.checkIdentityExist("xxx");
    Assert.assertFalse(exist);
  }
  
  @Test
  public void createIdentity()
  {
    String user = "charly7";
    boolean exist1 = target.checkIdentityExist(user);
    Assert.assertFalse(exist1);
    target.createIdentity(user, "pwd");
    boolean exist2 = target.checkIdentityExist(user);
    Assert.assertTrue(exist2);
  }
  
  @Test
  public void setPassword()
  {
    String user = "charly3";
    target.setPassword(user, "newpwd");
  }
  
  @Test
  public void authenticateTrue()
  {
    String user = "charly7";
    boolean a = target.authenticate(user, "pwd");
    Assert.assertEquals(true, a);
  }
  
  @Test
  public void authenticateFalse()
  {
    String user = "charly7";
    boolean a = target.authenticate(user, "bad");
    Assert.assertEquals(true, a);
  }
  
}
