package de.bitnoise.sonferenz.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import de.bitnoise.sonferenz.model.ActionModel;
import de.bitnoise.sonferenz.model.ProposalModel;
import de.bitnoise.sonferenz.model.UserModel;

public interface ActionRepository extends JpaRepository<ActionModel, Integer>
{

  ActionModel findByActionAndToken(String action, String token);

//  @Query("select t from TalkModel t where t.conference is not null")
//  Page<TalkModel> test(Pageable  request);

  Page<ActionModel> findByCreator(UserModel user, Pageable request);
}
