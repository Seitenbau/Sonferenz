package de.bitnoise.sonferenz.service.v2.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import de.bitnoise.sonferenz.model.TalkModel;

public interface TalkService
{

  List<TalkModel> getAllTalks();

  void deleteTalk(TalkModel talk);

  void saveTalk(TalkModel talk);

  TalkModel getTalkById(int id);

  int getCount();

  Page<TalkModel> getTalks(PageRequest request);

  long getVotableTalksCount();

  Page<TalkModel> getVotableTalks(PageRequest request);

  Page<TalkModel> getMyTalks(PageRequest request);

}
