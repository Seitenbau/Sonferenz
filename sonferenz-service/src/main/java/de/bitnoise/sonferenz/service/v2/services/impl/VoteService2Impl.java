package de.bitnoise.sonferenz.service.v2.services.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.model.VoteModel;
import de.bitnoise.sonferenz.repo.TalkRepository;
import de.bitnoise.sonferenz.repo.VoteRepository;
import de.bitnoise.sonferenz.service.v2.services.AuthenticationService;
import de.bitnoise.sonferenz.service.v2.services.VoteService;

@Service
public class VoteService2Impl implements VoteService
{
  @Autowired
  TalkRepository talkRepo;

  @Autowired
  VoteRepository voteRepo;

  @Autowired
  AuthenticationService auth;

  @Override
  @Transactional
  public void removeAllVotestForTalk(List<TalkModel> talks)
  {
    if (talks == null)
    {
      return;
    }
    for (TalkModel talk : talks)
    {
      List<VoteModel> votes = voteRepo.findByTalk(talk);
      if (votes != null)
      {
        for (VoteModel vote : votes)
        {
          voteRepo.delete(vote);
        }
      }
    }
  }

  @Override
  @Transactional
  public boolean vote(TalkModel talk, UserModel user, int increment)
  {
    ConferenceModel conference = talk.getConference();
    if (conference == null)
    {
      return false; // should not happen
    }
    VoteModel vote = voteRepo.findByUserAndTalk(user, talk);
    if (vote != null)
    {
      int neu = (vote.getRateing() == null ? increment : vote.getRateing()
          + increment);
      neu = (neu > 1 ? 1 : neu);
      neu = (neu < -1 ? -1 : neu);
      if (limitExeeded(conference, user, neu))
      {
        return false;
      }
      else
      {
        vote.setRateing(neu);
        voteRepo.save(vote);
      }
    }
    else
    {
      int neu = increment;
      neu = (neu > 1 ? 1 : neu);
      neu = (neu < -1 ? -1 : neu);
      if (limitExeeded(conference, user, neu))
      {
        return false;
      }

      vote = new VoteModel();
      vote.setTalk(talk);
      vote.setUser(user);
      vote.setRateing(0 + increment);
      voteRepo.save(vote);
    }
    return true;
  }

  private boolean limitExeeded(ConferenceModel conference, UserModel user,
      int neu)
  {
    if (neu >= 1)
    {
      Integer maxVotes = conference.getVotesPerUser();
      if (maxVotes == null)
      {
        return false;
      }
      List<VoteModel> x = getAllAttendingTalks(conference, user, 1);
      if (x != null && x.size() >= maxVotes)
      {
        return true;
      }
    }
    return false;
  }

  private List<VoteModel> getAllAttendingTalks(ConferenceModel conference,
      UserModel user, int minState)
  {
    List<VoteModel> rest = voteRepo.findByUserAndRateing(user, minState);
    for (VoteModel v : rest)
    {
      TalkModel t = v.getTalk();
      Hibernate.initialize(v);
      if (t != null)
      {
        Hibernate.initialize(t.getVotes());
      }
    }
    return rest;
  }

  @Override
  @Transactional
  public List<VoteModel> getMyVotes()
  {
    UserModel user = auth.getCurrentUser();
    if (user == null)
    {
      throw new IllegalStateException("No User logged in");
    }
    List<VoteModel> votes = voteRepo.findByUser(user);
    Hibernate.initialize(votes);
    return votes;
  }

  @Override
  @Transactional
  public void saveMyVotes(List<VoteModel> votes)
  {
    UserModel user = auth.getCurrentUser();
    voteRepo.deleteInBatch( voteRepo.findByUser(user) );
    for(VoteModel vote : votes) {
    voteRepo.saveAndFlush(vote);
    }
  }

}
