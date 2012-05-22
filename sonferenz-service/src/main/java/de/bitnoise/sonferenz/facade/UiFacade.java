package de.bitnoise.sonferenz.facade;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
import de.bitnoise.sonferenz.service.v2.actions.impl.SubscribeActionImpl.ActionCreateUser;

public interface UiFacade
{

  boolean checkMailNotExists(String mail);
  
  boolean checkUserNotExists(String username);

  void createIdentity(String provider, String username, String displayname, String password, String email,Collection<UserRoles> newRoles);

  void createIdentity(String provider, String username, String password, String email,Collection<UserRoles> newRoles);
  
  void deleteProposal(ProposalModel talk);

  void deleteSuggestion(SuggestionModel talk);

  void executeAction(ActionCreateUser data);

  ConferenceModel getActiveConference();

  Page<ConferenceModel> getAllConferences(Pageable pageable);

  int getAllConferencesCount();

  Page<ConfigurationModel> getAllConfigurations(PageRequest request);

  Page<UserRole> getAllRoles(PageRequest request);

  List<ProposalModel> getAllProposals();

  int getAllProposalsCount();

  List<ProposalModel> getAllProposalsForConference(ConferenceModel conference);

  List<UserModel> getAllUsers();

  Page<UserModel> getAllUsers(PageRequest request);

  Page<SuggestionModel> getAllSuggestions(PageRequest request);

  ConferenceModel getConference(int id);

  Page<ConferenceModel> getConferences(Pageable page);

  UserModel getCurrentUser();

  ProposalModel getProposalById(int id);

  Page<ProposalModel> getProposals(PageRequest request);

  String getText(String id);

  Page<StaticContentModel> getTexte(PageRequest request);

  Page<ActionModel> getUserActions(PageRequest request, UserModel user);

  int getUserCount();

  Page<ProposalModel> getVotableProposals(PageRequest request);

  long getVotableSuggestionCount();

  SuggestionModel getSuggestionById(int id);

  int getWhishesCount();

  Integer getWhishLikeCount(UserModel user, SuggestionModel whish);

  void likeSuggestion(UserModel user, SuggestionModel whish);

  void removeAllVotestForProposal(List<ProposalModel> asTalks);

  void removeTalksFromConference(ConferenceModel conference, List<ProposalModel> asTalks);

  void saveProposal(ProposalModel talk);

  void saveText(String id, String neu);

  void saveUser(UserModel user, Collection<UserRoles> newRoles);

  void saveSuggestion(SuggestionModel talk);

  void storeConference(ConferenceModel conference);

  void unLikeSuggestion(UserModel user, SuggestionModel whish);

  void unSuggest(SuggestionModel whish);

  void userUpdate(UserModel user, String newName);

  Aktion validateAction(String action, String token);

  boolean vote(ProposalModel talk, UserModel user, int increment);

  void createToken(String user, String mail,String body, String subject, String provider);
  
  List<String> availableProviders();

  Page<ActionModel> getAllUserActions(PageRequest request, UserModel user);

  void addProposalToConference(ConferenceModel conference,
      List<ProposalModel> asTalks);

}
