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
import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.model.UserRole;
import de.bitnoise.sonferenz.model.UserRoles;
import de.bitnoise.sonferenz.model.WhishModel;
import de.bitnoise.sonferenz.service.v2.actions.Aktion;
import de.bitnoise.sonferenz.service.v2.actions.impl.SubscribeActionImpl.ActionCreateUser;

public interface UiFacade
{

  void addTalksToConference(ConferenceModel conference, List<TalkModel> asTalks);

  boolean checkMailNotExists(String mail);
  
  boolean checkUserNotExists(String username);

  void createIdentity(String provider, String username, String displayname, String password, String email,Collection<UserRoles> newRoles);

  void createIdentity(String provider, String username, String password, String email,Collection<UserRoles> newRoles);
  
  void deleteTalk(TalkModel talk);

  void deleteWhish(WhishModel talk);

  void executeAction(ActionCreateUser data);

  ConferenceModel getActiveConference();

  Page<ConferenceModel> getAllConferences(Pageable pageable);

  int getAllConferencesCount();

  Page<ConfigurationModel> getAllConfigurations(PageRequest request);

  Page<UserRole> getAllRoles(PageRequest request);

  List<TalkModel> getAllTalks();

  int getAllTalksCount();

  List<TalkModel> getAllTalksForConference(ConferenceModel conference);

  List<UserModel> getAllUsers();

  Page<UserModel> getAllUsers(PageRequest request);

  Page<WhishModel> getAllWhishes(PageRequest request);

  ConferenceModel getConference(int id);

  Page<ConferenceModel> getConferences(Pageable page);

  UserModel getCurrentUser();

  TalkModel getTalkById(int id);

  Page<TalkModel> getTalks(PageRequest request);

  String getText(String id);

  Page<StaticContentModel> getTexte(PageRequest request);

  Page<ActionModel> getUserActions(PageRequest request, UserModel user);

  int getUserCount();

  Page<TalkModel> getVotableTalks(PageRequest request);

  long getVotableTalksCount();

  WhishModel getWhishById(int id);

  int getWhishesCount();

  Integer getWhishLikeCount(UserModel user, WhishModel whish);

  void likeWhish(UserModel user, WhishModel whish);

  void removeAllVotestForTalk(List<TalkModel> asTalks);

  void removeTalksFromConference(ConferenceModel conference, List<TalkModel> asTalks);

  void saveTalk(TalkModel talk);

  void saveText(String id, String neu);

  void saveUser(UserModel user, Collection<UserRoles> newRoles);

  void saveWhish(WhishModel talk);

  void storeConference(ConferenceModel conference);

  void unLikeWhish(UserModel user, WhishModel whish);

  void unwhish(WhishModel whish);

  void userUpdate(UserModel user, String newName);

  Aktion validateAction(String action, String token);

  boolean vote(TalkModel talk, UserModel user, int increment);

  void createToken(String user, String mail,String body, String subject);
  
  List<String> availableProviders();

}
