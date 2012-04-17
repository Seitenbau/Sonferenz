package de.bitnoise.sonferenz.model;

import java.util.Arrays;
import java.util.List;

public enum UserRoles
{
  NONE, ADMIN, USER, MANAGER, INVITE;

  public static List<? extends UserRoles> asList()
  {
    return Arrays.asList(values());
  }

  public static UserRoles of(UserRole role)
  {
    if (role == null  || role.getName() == null)
    {
      return NONE;
    }
    UserRoles val = valueOf(role.getName().toUpperCase());
    if (val != null)
    {
      return val;
    }
    return NONE;
  }

}
