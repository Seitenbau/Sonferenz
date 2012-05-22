package de.bitnoise.sonferenz.service.v2.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.model.SuggestionModel;

public interface SuggestionService
{

  int getCount();

  Page<SuggestionModel> getSuggestions(PageRequest request);

  void unSuggest(SuggestionModel whish);

  void deleteSuggestion(SuggestionModel whish);

  void saveSuggestion(SuggestionModel whish);

  SuggestionModel getSuggestionById(int id);

  void like(UserModel user, SuggestionModel whish);

  void unLike(UserModel user, SuggestionModel whish);

  Integer getSuggestionLikeCount(UserModel user, SuggestionModel whish);

  Page<SuggestionModel> getMySuggestions(PageRequest request);

}
