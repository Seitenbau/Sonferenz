package de.bitnoise.sonferenz.web.pages.conference.table;

import java.io.Serializable;

import de.bitnoise.sonferenz.model.ProposalModel;

public class TalkToConference implements Serializable
{
  public Integer id;
  public String title;
  public String author;
  public ProposalModel talk;

  @Override
  public boolean equals(Object arg0)
  {
    if (arg0 == null)
    {
      return false;
    }
    TalkToConference other = (TalkToConference) arg0;
    return id.equals(other.id);
  }
}
