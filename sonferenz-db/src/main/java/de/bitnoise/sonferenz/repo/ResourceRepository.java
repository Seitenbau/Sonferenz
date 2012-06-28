package de.bitnoise.sonferenz.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import de.bitnoise.sonferenz.model.ResourceModel;

public interface ResourceRepository extends JpaRepository<ResourceModel, Integer>
{
}
