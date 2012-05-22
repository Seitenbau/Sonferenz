package de.bitnoise.sonferenz.service.v2.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import de.bitnoise.sonferenz.model.LikeModel;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.model.SuggestionModel;
import de.bitnoise.sonferenz.repo.LikeRepository;
import de.bitnoise.sonferenz.repo.SuggestionRepository;
import de.bitnoise.sonferenz.service.v2.services.AuthenticationService;
import de.bitnoise.sonferenz.service.v2.services.SuggestionService;

@Service
public class SuggestionService2Impl implements SuggestionService
{

  @Autowired
  SuggestionRepository whishRepo;

  @Autowired
  LikeRepository likeRepo;

  @Autowired
  AuthenticationService authService;

  @Override
  @Transactional(readOnly = true)
  public int getCount()
  {
    return (int) whishRepo.count();
  }

  @Override
  @Transactional(readOnly = true)
  public Page<SuggestionModel> getSuggestions(PageRequest request)
  {
    return whishRepo.findAll(request);
  }

  @Override
  @Transactional 
  public void unSuggest(SuggestionModel whish)
  {
    UserModel user = authService.getCurrentUser();
  }

  @Override
  @Transactional 
  public void deleteSuggestion(SuggestionModel whish)
  {
    whishRepo.delete(whish);
  }

  @Override
  @Transactional 
  public void saveSuggestion(SuggestionModel whish)
  {
    if (whish.getOwner() == null)
    {
      UserModel user = authService.getCurrentUser();
      whish.setOwner(user);
    }
    whishRepo.save(whish);
  }

  @Override
  public SuggestionModel getSuggestionById(int id)
  {
    return whishRepo.findOne(id);
  }

  @Override
  @Transactional 
  public void like(UserModel user, SuggestionModel whish)
  {
    LikeModel current = likeRepo.findByUserAndWhish(user, whish);
    if (current == null)
    {
      current = new LikeModel();
      current.setUser(user);
      current.setWhish(whish);
    }

    current.setLikes(1);
    likeRepo.save(current);

    Integer old = whish.getLikes();
    whish.setLikes(old == null ? 1 : old + 1);
    whishRepo.save(whish);
  }

  @Override
  @Transactional 
  public void unLike(UserModel user, SuggestionModel whish)
  {
    LikeModel current = likeRepo.findByUserAndWhish(user, whish);
    if (current == null)
    {
      return;
    }

    current.setLikes(0);
    likeRepo.save(current);

    Integer old = whish.getLikes();
    whish.setLikes(old == null ? 0 : old - 1);
    whishRepo.save(whish);
  }

  @Override
  @Transactional(readOnly = true)
  public Integer getSuggestionLikeCount(UserModel user, SuggestionModel whish)
  {
    LikeModel current = likeRepo.findByUserAndWhish(user, whish);
    if (current == null)
    {
      return null;
    }
    return current.getLikes();
  }

  @Override
  @Transactional(readOnly = true)
  public Page<SuggestionModel> getMySuggestions(PageRequest request) {
	  UserModel current = authService.getCurrentUserOrFail();
	  Page<SuggestionModel> result = whishRepo.findAllByOwner(current,request);
	  return result;
  }

}
