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
import de.bitnoise.sonferenz.model.ProposalModel;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.model.UserRole;
import de.bitnoise.sonferenz.model.UserRoles;
import de.bitnoise.sonferenz.model.SuggestionModel;
import de.bitnoise.sonferenz.service.v2.actions.Aktion;
import de.bitnoise.sonferenz.service.v2.actions.impl.SubscribeActionImpl;
import de.bitnoise.sonferenz.service.v2.actions.impl.SubscribeActionImpl.ActionCreateUser;
import de.bitnoise.sonferenz.service.v2.services.ActionService;
import de.bitnoise.sonferenz.service.v2.services.AuthenticationService;
import de.bitnoise.sonferenz.service.v2.services.ConferenceService;
import de.bitnoise.sonferenz.service.v2.services.ConfigurationService;
import de.bitnoise.sonferenz.service.v2.services.StaticContentService;
import de.bitnoise.sonferenz.service.v2.services.ProposalService;
import de.bitnoise.sonferenz.service.v2.services.UserService;
import de.bitnoise.sonferenz.service.v2.services.VoteService;
import de.bitnoise.sonferenz.service.v2.services.SuggestionService;
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
  ProposalService _proposals;

  @Autowired
  UserService userFacade;

  @Autowired
  AuthenticationService authService;

  @Autowired
  SuggestionService _suggestions;

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
  public void deleteProposal(ProposalModel talk)
  {
    _proposals.deleteProposal(talk);
  }

  @Override
  public void saveProposal(ProposalModel talk)
  {
    _proposals.saveProposal(talk);
  }

  @Override
  public ProposalModel getProposalById(int id)
  {
    return _proposals
        .getProposalById(id);
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
  public Page<ProposalModel> getProposals(PageRequest request)
  {
    return _proposals.getProposals(request);
  }

  @Override
  public int getAllProposalsCount()
  {
    return _proposals.getCount();
  }

  @Override
  public int getAllConferencesCount()
  {
    return _conference.getCount();
  }

  @Override
  public int getWhishesCount()
  {
    return _suggestions.getCount();
  }

  @Override
  public Page<SuggestionModel> getAllSuggestions(PageRequest request)
  {
    return _suggestions.getSuggestions(request);
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
  public void unSuggest(SuggestionModel whish)
  {
    _suggestions.unSuggest(whish);
  }

  @Override
  public void deleteSuggestion(SuggestionModel whish)
  {
    _suggestions.deleteSuggestion(whish);
  }

  @Override
  public void saveSuggestion(SuggestionModel whish)
  {
    _suggestions.saveSuggestion(whish);
  }

  @Override
  public SuggestionModel getSuggestionById(int id)
  {
    return _suggestions.getSuggestionById(id);
  }

  @Override
  public void likeSuggestion(UserModel user, SuggestionModel whish)
  {
    _suggestions.like(user, whish);
  }

  @Override
  public void unLikeSuggestion(UserModel user, SuggestionModel whish)
  {
    _suggestions.unLike(user, whish);
  }

  @Override
  public Integer getWhishLikeCount(UserModel user, SuggestionModel whish)
  {
    return _suggestions.getSuggestionLikeCount(user, whish);
  }

  @Override
  public List<ProposalModel> getAllProposals()
  {
    return _proposals.getAllProposals();
  }

  @Override
  public List<ProposalModel> getAllProposalsForConference(ConferenceModel conference)
  {
    return _conference.getAllProposalsForConference(conference);
  }

  @Override
  public void removeAllVotestForProposal(List<ProposalModel> talks)
  {
    voteService.removeAllVotestForTalk(talks);
  }

  @Override
  public void removeTalksFromConference(ConferenceModel conference, List<ProposalModel> asTalks)
  {
    _conference.removeProposalsFromConference(conference, asTalks);
  }

  @Override
  public void addProposalToConference(ConferenceModel conference, List<ProposalModel> asTalks)
  {
    _conference.addProposalsToConference(conference, asTalks);
  }

  @Override
  public boolean vote(ProposalModel talk, UserModel user, int increment)
  {
    return voteService.vote(talk, user, increment);
  }

  @Override
  public long getVotableSuggestionCount()
  {
    return _proposals.getVotableProposalsCount();
  }

  @Override
  public Page<ProposalModel> getVotableProposals(PageRequest request)
  {
    return _proposals.getVotableProposals(request);
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
