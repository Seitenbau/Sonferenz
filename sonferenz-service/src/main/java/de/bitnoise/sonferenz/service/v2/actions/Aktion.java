package de.bitnoise.sonferenz.service.v2.actions;

/**
 * Wrapper around a Action
 */
public class Aktion
{
  String action;

  String token;

  public Aktion(Integer tokenId, String actionName, String token,
      ActionState state)
  {
    this.action = actionName;
    this.data = state;
    this.token = token;
    this.id = tokenId;
  }

  public String getAction()
  {
    return action;
  }

  public ActionState getData()
  {
    return data;
  }

  ActionState data;

  Integer id;

  public String getToken()
  {
    return token;
  }

  public Integer getId()
  {
    return id;
  }

}
