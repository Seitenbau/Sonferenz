package de.bitnoise.sonferenz.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.model.ProposalModel;
import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.model.UserModel;

public interface TalkRepository extends JpaRepository<TalkModel, Integer>
{
  Page<TalkModel> findByOwner(UserModel owner, Pageable page);

  Page<TalkModel> findByConference(ConferenceModel conference, Pageable page);
}
