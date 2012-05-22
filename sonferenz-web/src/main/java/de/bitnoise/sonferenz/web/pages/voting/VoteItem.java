package de.bitnoise.sonferenz.web.pages.voting;

import java.io.Serializable;

import de.bitnoise.sonferenz.model.ProposalModel;

class VoteItem implements Serializable
{

  ProposalModel _talk;

  Integer _position;

  public VoteItem(Integer rating, ProposalModel talk)
  {
    _position=rating;
    _talk = talk;
  }

  public Integer getRateing()
  {
    return _position;
  }

  public ProposalModel getTalk()
  {
    return _talk;
  }
  
}