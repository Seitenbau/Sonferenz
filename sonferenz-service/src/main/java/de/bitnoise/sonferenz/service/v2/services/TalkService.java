package de.bitnoise.sonferenz.service.v2.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import de.bitnoise.sonferenz.model.ProposalModel;
import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.service.v2.model.AppContext;

public interface TalkService
{
  Page<TalkModel> getMyTalks(AppContext context, PageRequest request);

  Page<TalkModel> getTalks(PageRequest request);

  long getAllTalksCount();

  void saveTalk(TalkModel talk);

  TalkModel getTalkById(Long id);
}
