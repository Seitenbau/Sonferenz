package de.bitnoise.sonferenz.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import de.bitnoise.sonferenz.model.LikeModel;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.model.SuggestionModel;

public interface LikeRepository extends JpaRepository<LikeModel, Integer>
{

  LikeModel findByUserAndWhish(UserModel user, SuggestionModel whish);
}
