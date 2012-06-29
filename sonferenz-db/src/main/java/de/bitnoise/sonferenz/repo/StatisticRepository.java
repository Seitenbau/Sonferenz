package de.bitnoise.sonferenz.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import de.bitnoise.sonferenz.model.StatisticModel;

public interface StatisticRepository extends JpaRepository<StatisticModel, Integer>
{

  StatisticModel findByTypeAndName(String type, String objectId);
}
