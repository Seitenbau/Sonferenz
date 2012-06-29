package de.bitnoise.sonferenz.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import de.bitnoise.sonferenz.model.StatisticRecordModel;

public interface StatisticRecordRepository extends JpaRepository<StatisticRecordModel, Integer>
{
}
