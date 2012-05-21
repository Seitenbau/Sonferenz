package de.bitnoise.sonferenz.service.v2.services;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import de.bitnoise.sonferenz.model.UserModel;

public class VotedItem implements Serializable
{
  String _title;

  Integer _votes;

  Integer _absoluteVotes;
  
  Set<String> _users;
  
  Boolean _checked;

  public Boolean getChecked() {
    return _checked;
  }

  public void setChecked(Boolean _checked) {
    this._checked = _checked;
  }

  public String getTitle()
  {
    return _title;
  }

  public void setTitle(String title)
  {
    _title = title;
  }

  public Integer getVotes()
  {
    return _votes;
  }

  public void setVotes(Integer votes)
  {
    _votes = votes;
  }
  

  public void setAbsoluteVotes(Integer avotes) {
    _absoluteVotes=avotes;
  }

  public Integer getAbsoluteVotes() {
    return _absoluteVotes;
  }

  public void setTalk(String title)
  {
    _title = title;
  }

  public void addUser(UserModel user) {
    if (_users == null) {
      _users = new HashSet<String>();
    }
    _users.add(user.getName());
  }

  public Set<String> getUsers() {
    if (_users == null) {
      return new HashSet<String>();
    }
    return _users;
  }

}
