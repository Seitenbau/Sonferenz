package de.bitnoise.sonferenz.service.v2.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.bitnoise.sonferenz.model.SpeakerModel;
import de.bitnoise.sonferenz.service.v2.exceptions.RepositoryException;

public interface SpeakerService {

  Page<SpeakerModel> getAllSpeakers(Pageable pageable) throws RepositoryException;

  SpeakerModel getSpeakerById(Long id);

  void saveSpeaker(SpeakerModel _speaker);
}
