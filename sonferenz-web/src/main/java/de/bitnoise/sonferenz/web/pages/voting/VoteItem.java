package de.bitnoise.sonferenz.web.pages.voting;

import java.io.Serializable;

import de.bitnoise.sonferenz.model.TalkModel;

class VoteItem implements Serializable
{

  TalkModel _talk;

  Integer _position;

  public VoteItem(Integer rating, TalkModel talk)
  {
    _position=rating;
    _talk = talk;
  }

  public Integer getRateing()
  {
    return _position;
  }

  public TalkModel getTalk()
  {
    return _talk;
  }
  
}