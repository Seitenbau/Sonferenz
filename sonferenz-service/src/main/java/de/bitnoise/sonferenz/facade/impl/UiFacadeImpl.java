package de.bitnoise.sonferenz.facade.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.ActionModel;
import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.model.ConfigurationModel;
import de.bitnoise.sonferenz.model.StaticContentModel;
import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.model.UserRole;
import de.bitnoise.sonferenz.model.UserRoles;
import de.bitnoise.sonferenz.model.WhishModel;
import de.bitnoise.sonferenz.service.v2.actions.Aktion;
import de.bitnoise.sonferenz.service.v2.actions.impl.SubscribeActionImpl;
import de.bitnoise.sonferenz.service.v2.actions.impl.SubscribeActionImpl.ActionCreateUser;
import de.bitnoise.sonferenz.service.v2.services.ActionService;
import de.bitnoise.sonferenz.service.v2.services.AuthenticationService;
import de.bitnoise.sonferenz.service.v2.services.ConferenceService;
import de.bitnoise.sonferenz.service.v2.services.ConfigurationService;
import de.bitnoise.sonferenz.service.v2.services.StaticContentService;
import de.bitnoise.sonferenz.service.v2.services.TalkService;
import de.bitnoise.sonferenz.service.v2.services.UserService;
import de.bitnoise.sonferenz.service.v2.services.VoteService;
import de.bitnoise.sonferenz.service.v2.services.WhishService;
import de.bitnoise.sonferenz.service.v2.services.idp.IdpService;

@Service
public class UiFacadeImpl implements UiFacade
{
  @Autowired
  ConferenceService _conference;

  @Autowired
  ConfigurationService _config;

  @Autowired
  ActionService _actions;

  @Autowired
  StaticContentService content;

  @Autowired
  TalkService _talks;

  @Autowired
  UserService userFacade;

  @Autowired
  AuthenticationService authService;

  @Autowired
  WhishService whishService;

  @Autowired
  VoteService voteService;
  
  @Autowired
  IdpService idpService;

  @Override
  public ConferenceModel getActiveConference()
  {
    return _conference.getActiveConference();
  }

  @Override
  public void storeConference(ConferenceModel conference)
  {
    _conference.updateConference(conference);
  }

  @Override
  public Page<ConferenceModel> getConferences(Pageable page)
  {
    return _conference.getConferences(page);
  }

  @Override
  public UserModel getCurrentUser()
  {
    return authService.getCurrentUser();
  }

  @Override
  public void deleteTalk(TalkModel talk)
  {
    _talks.deleteTalk(talk);
  }

  @Override
  public void saveTalk(TalkModel talk)
  {
    _talks.saveTalk(talk);
  }

  @Override
  public TalkModel getTalkById(int id)
  {
    return _talks
        .getTalkById(id);
  }

  @Override
  public String getText(String key)
  {
    return content.text(key);
  }

  @Override
  public void saveText(String key, String neu)
  {
    content.storeText(key, neu);
  }

  @Override
  public Page<TalkModel> getTalks(PageRequest request)
  {
    return _talks.getTalks(request);
  }

  @Override
  public int getAllTalksCount()
  {
    return _talks.getCount();
  }

  @Override
  public int getAllConferencesCount()
  {
    return _conference.getCount();
  }

  @Override
  public int getWhishesCount()
  {
    return whishService.getCount();
  }

  @Override
  public Page<WhishModel> getAllWhishes(PageRequest request)
  {
    return whishService.getWhishes(request);
  }

  @Override
  public int getUserCount()
  {
    return userFacade.getCount();
  }

  @Override
  public Page<UserModel> getAllUsers(PageRequest request)
  {
    return userFacade.getAllUsers(request);
  }

  @Override
  public List<UserModel> getAllUsers()
  {
    return userFacade.getAllUsers();
  }

  @Override
  public void createIdentity(String provider, String username, String displayname, String password, String email, Collection<UserRoles> newRoles)
  {
    userFacade.createIdentity(provider, username, displayname, password, email, newRoles);
  } 
  
  @Override
  public void createIdentity(String provider, String username, String password, String email, Collection<UserRoles> newRoles)
  {
    this.createIdentity(provider, username, username, password, email, newRoles);
  } 

  @Override
  public void saveUser(UserModel user, Collection<UserRoles> newRoles)
  {
    userFacade.saveUser(user, newRoles);
  }

  @Override
  public void unwhish(WhishModel whish)
  {
    whishService.unWhish(whish);
  }

  @Override
  public void deleteWhish(WhishModel whish)
  {
    whishService.deleteWhish(whish);
  }

  @Override
  public void saveWhish(WhishModel whish)
  {
    whishService.saveWhish(whish);
  }

  @Override
  public WhishModel getWhishById(int id)
  {
    return whishService.getWhishById(id);
  }

  @Override
  public void likeWhish(UserModel user, WhishModel whish)
  {
    whishService.like(user, whish);
  }

  @Override
  public void unLikeWhish(UserModel user, WhishModel whish)
  {
    whishService.unLike(user, whish);
  }

  @Override
  public Integer getWhishLikeCount(UserModel user, WhishModel whish)
  {
    return whishService.getWhishLikeCount(user, whish);
  }

  @Override
  public List<TalkModel> getAllTalks()
  {
    return _talks.getAllTalks();
  }

  @Override
  public List<TalkModel> getAllTalksForConference(ConferenceModel conference)
  {
    return _conference.getAllTalksForConference(conference);
  }

  @Override
  public void removeAllVotestForTalk(List<TalkModel> talks)
  {
    voteService.removeAllVotestForTalk(talks);
  }

  @Override
  public void removeTalksFromConference(ConferenceModel conference, List<TalkModel> asTalks)
  {
    _conference.removeTalksFromConference(conference, asTalks);
  }

  @Override
  public void addTalksToConference(ConferenceModel conference, List<TalkModel> asTalks)
  {
    _conference.addTalksToConference(conference, asTalks);
  }

  @Override
  public boolean vote(TalkModel talk, UserModel user, int increment)
  {
    return voteService.vote(talk, user, increment);
  }

  @Override
  public long getVotableTalksCount()
  {
    return _talks.getVotableTalksCount();
  }

  @Override
  public Page<TalkModel> getVotableTalks(PageRequest request)
  {
    return _talks.getVotableTalks(request);
  }

  @Override
  public Page<ConferenceModel> getAllConferences(Pageable pageable)
  {
    return _conference.getConferences(pageable);
  }

  @Override
  public ConferenceModel getConference(int id)
  {
    return _conference.getConference(id);
  }

  @Override
  public Aktion validateAction(String action, String token)
  {
    return _actions.loadAction(action, token);
  }

  @Override
  public void userUpdate(UserModel user, String newName)
  {
    userFacade.updateUser(user, newName);
  }

  @Override
  public void executeAction(ActionCreateUser data)
  {
    _actions.execute(data);
  }

  @Override
  public boolean checkMailNotExists(String mail)
  {
    return userFacade.checkMailNotExists(mail);
  }

  @Override
  public Page<UserRole> getAllRoles(PageRequest request)
  {
    return userFacade.getAllRoles(request);
  }

  @Override
  public Page<ConfigurationModel> getAllConfigurations(PageRequest request)
  {
    return _config.getAllConfigurations(request);
  }

  @Override
  public Page<StaticContentModel> getTexte(PageRequest request)
  {
    return content.getAll(request);
  }

  @Override
  public Page<ActionModel> getUserActions(PageRequest request, UserModel user)
  {
    return _actions.getUserActions(request, user);
  }

  @Autowired
  SubscribeActionImpl _actionSubscribe;
  
  @Override
  public void createToken(String user, String mail,String body,String subject, String provider)
  {
    _actionSubscribe.createNewUserToken(user, mail, body,subject,provider);
  }

  @Override
  public List<String> availableProviders()
  {
    return idpService.getAvailableProviders();
  }

  @Override
  public boolean checkUserNotExists(String username) 
  {
    return userFacade.checkUserNotExists(username);
  }

  @Override
  public Page<ActionModel> getAllUserActions(PageRequest request, UserModel user) {
	  return _actions.getAllUserActions(request, user);
  }
}
