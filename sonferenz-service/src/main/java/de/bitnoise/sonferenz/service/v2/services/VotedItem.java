package de.bitnoise.sonferenz.service.v2.services;

public class VotedItem
{
  String _title;

  Integer _votes;

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

  public void setTalk(String title)
  {
    _title = title;
  }

}
