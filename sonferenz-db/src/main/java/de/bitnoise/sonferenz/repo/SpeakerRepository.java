package de.bitnoise.sonferenz.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import de.bitnoise.sonferenz.model.SpeakerModel;

public interface SpeakerRepository extends JpaRepository<SpeakerModel, Integer>
{
  SpeakerModel findByName(String name);
}
