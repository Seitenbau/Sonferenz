package de.bitnoise.sonferenz.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.model.VoteModel;

public interface VoteRepository extends JpaRepository<VoteModel, Integer>
{

  List<VoteModel> findByTalk(TalkModel talk);

  VoteModel findByUserAndTalk(UserModel user, TalkModel talk);

  @Query("select  v from   VoteModel v  where v.user = ?1  and v.rateing > ?2")
  List<VoteModel> findByUserAndRateing(UserModel user, int rateing);

  List<VoteModel> findByUser(UserModel user);

}
