package de.bitnoise.sonferenz.web.pages.admin.model;

import java.io.Serializable;

import de.bitnoise.sonferenz.model.UserModel;

public class UserListItem implements Serializable
{
  public String name;
  public String roles;
  public String provider;
  public String login;
  public UserModel user;
}
