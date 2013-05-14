package de.bitnoise.sonferenz.service.v2.services.impl;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.model.ProposalModel;
import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.repo.TalkRepository;
import de.bitnoise.sonferenz.service.v2.model.AppContext;
import de.bitnoise.sonferenz.service.v2.services.AuthenticationService;
import de.bitnoise.sonferenz.service.v2.services.TalkService;

@Service
public class TalkServiceImpl implements TalkService
{
  @Autowired
  AuthenticationService authService;

  @Autowired
  TalkRepository talkRepo;

  @Override
  public Page<TalkModel> getMyTalks(AppContext context, PageRequest request)
  {
    UserModel current = authService.getCurrentUserOrFail();
    return talkRepo.findByOwner(current,request);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<TalkModel> getTalks(PageRequest request)
  {
    return talkRepo.findAll(request);
  }
  
  @Override
  @Transactional(readOnly = true)
  public Page<TalkModel> getTalks(ConferenceModel conference,PageRequest request)
  {
    return talkRepo.findByConference(conference,request);
  }

  @Override
  @Transactional(readOnly = true)
  public long getAllTalksCount()
  {
    return talkRepo.count();
  }

  @Override
  public void saveTalk(TalkModel talk)
  {
    talkRepo.save(talk);
  }

  @Override
  @Transactional
  public TalkModel getTalkById(Long id)
  {
    if(id==null) {
      return null;
    }
    TalkModel result = talkRepo.findOne(id.intValue());
    if(result!= null) {
      Hibernate.initialize(result.getSpeakers());
      Hibernate.initialize(result.getResources());
    }
    return result;
  }

}
