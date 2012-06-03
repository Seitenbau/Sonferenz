package de.bitnoise.sonferenz.service.v2.services.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import static de.bitnoise.sonferenz.service.v2.services.verify.VerifyParam.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import de.bitnoise.sonferenz.model.SpeakerModel;
import de.bitnoise.sonferenz.repo.SpeakerRepository;
import de.bitnoise.sonferenz.service.v2.exceptions.RepositoryException;
import de.bitnoise.sonferenz.service.v2.services.SpeakerService;

@Service
public class SpeakerServiceImpl implements SpeakerService {

  @Autowired
  protected SpeakerRepository speakerRepo;

  @Override
  public Page<SpeakerModel> getAllSpeakers(Pageable pageable) throws RepositoryException {
    verify(pageable).as("pageable").isNotNull();
    Page<SpeakerModel> result = speakerRepo.findAll(pageable);
    return result;
  }

  @Override
  public SpeakerModel getSpeakerById(Long id) {
    verify(id).as("id").isNotNull();
    SpeakerModel one = speakerRepo.findOne(id.intValue());
    return one;
  }

  @Override
  public void saveSpeaker(SpeakerModel speaker) {
    verify(speaker).as("speaker").isNotNull();
    if (speaker.isNew()) {
      speaker.setCreatedAt(new Date());
    }
    speakerRepo.saveAndFlush(speaker);
  }
}
