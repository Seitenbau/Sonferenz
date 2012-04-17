package de.bitnoise.sonferenz.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.model.WhishModel;

public interface WhishRepository extends JpaRepository<WhishModel, Integer>
{

	Page<WhishModel> findAllByOwner(UserModel current);
}
