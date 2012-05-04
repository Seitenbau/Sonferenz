package de.bitnoise.sonferenz.service.v2.services;

import java.util.List;

import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.model.VoteModel;

public interface VoteService
{

  void removeAllVotestForTalk(List<TalkModel> talks);

  boolean vote(TalkModel talk, UserModel user, int increment);

  List<VoteModel> getMyVotes();

  void saveMyVotes(List<VoteModel> votes);

  List<VotedItem> getVoteLevel(ConferenceModel conference);

  List<UserModel> getAllUsersNotVoted(ConferenceModel conference);

}
