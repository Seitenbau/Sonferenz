package de.bitnoise.sonferenz.service.v2.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.model.ConferenceState;
import de.bitnoise.sonferenz.model.ProposalModel;
import de.bitnoise.sonferenz.service.v2.exceptions.GeneralConferenceException;

public interface ConferenceService
{
  public ConferenceModel getActiveConference2()
      throws GeneralConferenceException;

  public void updateConference(ConferenceModel conference)
      throws GeneralConferenceException;

  public Page<ConferenceModel> getConferences(Pageable page)
      throws GeneralConferenceException;

  public List<ProposalModel> getAllProposalsForConference(ConferenceModel _conference)
      throws GeneralConferenceException;

  public void removeProposalsFromConference(ConferenceModel _conference, List<ProposalModel> asTalks)
      throws GeneralConferenceException;

  public void addProposalsToConference(ConferenceModel _conference, List<ProposalModel> asTalks)
      throws GeneralConferenceException;

  public int getCount();

  public ConferenceModel getConference(int id);

  public void addTalkForProposalToConference(ConferenceModel conference,
      Integer proposalId);

  public void setState(ConferenceModel conference, ConferenceState voted);
}
