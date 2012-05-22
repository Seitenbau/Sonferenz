package de.bitnoise.sonferenz.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.model.SuggestionModel;

public interface SuggestionRepository extends JpaRepository<SuggestionModel, Integer>
{

	Page<SuggestionModel> findAllByOwner(UserModel current,Pageable page);
}
