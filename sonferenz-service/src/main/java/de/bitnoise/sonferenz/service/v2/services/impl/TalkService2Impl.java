package de.bitnoise.sonferenz.service.v2.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.repo.TalkRepository;
import de.bitnoise.sonferenz.service.v2.Detach;
import de.bitnoise.sonferenz.service.v2.services.TalkService;

@Service
public class TalkService2Impl implements TalkService
{
  @Autowired
  TalkRepository talkRepo;

  @Override
  @Transactional(readOnly=true)
  public List<TalkModel> getAllTalks()
  {
    return talkRepo.findAll();
  }

  @Override
  @Transactional 
  public void deleteTalk(TalkModel talk)
  {
    talkRepo.delete(talk);
  }

  @Override
  @Transactional 
  public void saveTalk(TalkModel talk)
  {
    talkRepo.save(talk);
  }

  @Override
  @Transactional(readOnly=true)
  public TalkModel getTalkById(int id)
  {
    return talkRepo.findOne(id);
  }
  @Override
  @Transactional(readOnly=true)
  public int getCount()
  {
    return (int) talkRepo.count();
  }

  @Override
  @Transactional(readOnly=true)
  public Page<TalkModel> getTalks(PageRequest request)
  {
    return talkRepo.findAll(request);
  }

  @Override
  @Transactional(readOnly=true)
  public long getVotableTalksCount()
  {
    return talkRepo.countAllVotable();
  }

  @Override
  @Transactional(readOnly=true)
  public Page<TalkModel> getVotableTalks(PageRequest request)
  {
    Page<TalkModel> result = talkRepo.test(request);
    Detach.detachTM(result) ;
    return  result;
  }

}
