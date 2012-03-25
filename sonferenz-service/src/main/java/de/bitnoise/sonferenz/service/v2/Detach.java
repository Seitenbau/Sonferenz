package de.bitnoise.sonferenz.service.v2;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;

import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.model.WhishModel;

public class Detach
{

  public static UserModel detach(UserModel user)
  {
    if (user != null)
    {
      Hibernate.initialize(user);
      Hibernate.initialize(user.getRoles());
      Hibernate.initialize(user.getProvider());
    }
    return user;
  }

  public static List<UserModel> detach(List<UserModel> result)
  {
    if (result != null)
    {
      for (UserModel model : result)
      {
        detach(model);
      }
    }
    return result;
  }

  public static ConferenceModel detach(ConferenceModel item)
  {
    Hibernate.initialize(item);
    return item;
  }

  public static TalkModel detach(TalkModel item)
  {
    Hibernate.initialize(item);
    Hibernate.initialize(item.getVotes());
    return item;
  }

  public static void detach(WhishModel item)
  {
    Hibernate.initialize(item);
  }

  public static Page<UserModel> detachUM(Page<UserModel> findAll)
  {
    detach(findAll.getContent());
    return findAll;
  }

  public static void detachTM(Page<TalkModel> result)
  {
    if (result == null || result.getContent() == null)
    {
      return;
    }
    for (TalkModel t : result)
    {
      Hibernate.initialize(t);
      Hibernate.initialize(t.getVotes());
    }
  }

}
