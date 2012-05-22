package de.bitnoise.sonferenz.service.v2.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.model.ConferenceState;
import de.bitnoise.sonferenz.model.ProposalModel;
import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.repo.ConferenceRepository;
import de.bitnoise.sonferenz.repo.ProposalRepository;
import de.bitnoise.sonferenz.repo.TalkRepository;
import de.bitnoise.sonferenz.service.v2.exceptions.GeneralConferenceException;
import de.bitnoise.sonferenz.service.v2.exceptions.RepositoryException;
import de.bitnoise.sonferenz.service.v2.services.ConferenceService;

@Service
public class ConferenceService2Impl implements ConferenceService
{
  @Autowired
  ConferenceRepository conferenceRepo;
  
  @Autowired
  ProposalRepository proposalRepo;
  
  @Autowired
  TalkRepository talkRepo;

  @Override
  public ConferenceModel getActiveConference()
  {
    try
    {
      ConferenceModel current = conferenceRepo.findByActive(true);
      return current;
    }
    catch (Throwable t)
    {
      throw new RepositoryException(t);
    }
  }

  @Override
  public void updateConference(ConferenceModel conference)
      throws GeneralConferenceException
  {
    try
    {
      conferenceRepo.save(conference);
    }
    catch (Throwable t)
    {
      throw new RepositoryException(t);
    }
  }

  @Override
  public Page<ConferenceModel> getConferences(Pageable page)
      throws GeneralConferenceException
  {
    try
    {
      return conferenceRepo.findAll(page);
    }
    catch (Throwable t)
    {
      throw new RepositoryException(t);
    }

  }

  @Override
  public List<ProposalModel> getAllProposalsForConference(ConferenceModel conference) throws GeneralConferenceException
  {
    return proposalRepo.findAllByConference(conference);
  }

  @Override
  public void removeProposalsFromConference(ConferenceModel _conference, List<ProposalModel> talksToRemove)
      throws GeneralConferenceException
  {
    for (ProposalModel talk : talksToRemove)
    {
      talk.setConference(null);
      proposalRepo.save(talk);
    }
  }

  @Override
  public void addProposalsToConference(ConferenceModel conference, List<ProposalModel> talksToAdd)
      throws GeneralConferenceException
  {
    for (ProposalModel talk : talksToAdd)
    {
      talk.setConference(conference);
      proposalRepo.save(talk);
    }
  }

  @Override
  public int getCount()
  {
    return (int) conferenceRepo.count();
  }

  @Override
  public ConferenceModel getConference(int id)
  {
    return conferenceRepo.findOne(id);
  }

  @Override
  public void addTalkForProposalToConference(ConferenceModel conference,
      Integer proposalId)
  {
    ProposalModel proposal = proposalRepo.findOne(proposalId);
    TalkModel talk = new TalkModel();
    talk.setConference(conference);
    talk.setDescription(proposal.getDescription());
    talk.setOwner(proposal.getOwner());
    talk.setTitle(proposal.getTitle());
    talk.setAuthor(proposal.getAuthor());
    
    talkRepo.save(talk);
  }

  @Override
  public void setState(ConferenceModel conference, ConferenceState voted)
  {
    conference.setState(voted);
    conferenceRepo.save(conference);
  }


}
