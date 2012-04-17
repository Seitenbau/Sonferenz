package de.bitnoise.sonferenz.service.v2.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.model.WhishModel;

public interface WhishService
{

  int getCount();

  Page<WhishModel> getWhishes(PageRequest request);

  void unWhish(WhishModel whish);

  void deleteWhish(WhishModel whish);

  void saveWhish(WhishModel whish);

  WhishModel getWhishById(int id);

  void like(UserModel user, WhishModel whish);

  void unLike(UserModel user, WhishModel whish);

  Integer getWhishLikeCount(UserModel user, WhishModel whish);

  Page<WhishModel> getMyWhishes(PageRequest request);

}
