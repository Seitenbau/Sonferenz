package de.bitnoise.sonferenz.service.v2.services.impl;

import static org.fest.assertions.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Date;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.bitnoise.sonferenz.model.SpeakerModel;
import de.bitnoise.sonferenz.repo.SpeakerRepository;
import de.bitnoise.sonferenz.service.v2.BaseTestClass;

public class SpeakerServiceImplTest extends BaseTestClass {
  @Mock
  SpeakerRepository speakerRepo;

  @InjectMocks
  SpeakerServiceImpl sut = new SpeakerServiceImpl();

  @Mock
  Pageable pageable;

  @Test
  public void getAllSpeakers_invalidParameter() {
    // verify
    expectedException(IllegalArgumentException.class, "pageable was null");

    // execute
    sut.getAllSpeakers(null);
  }

  @Test
  public void getAllSpeakers_emptyDbResults() {
    // prepare
    Page<SpeakerModel> expected = null;
    when(speakerRepo.findAll(pageable)).thenReturn(expected);

    // execute
    Page<SpeakerModel> result = sut.getAllSpeakers(pageable);

    // verify
    assertThat(result == expected).isTrue();
  }

  @Test
  public void getSpeakerById_invalidParameter() {
    // verify
    expectedException(IllegalArgumentException.class, "id was null");

    // execute
    sut.getSpeakerById(null);
  }

  @Test
  public void getSpeakerById_findNone() {
    // prepare
    when(speakerRepo.findOne(23)).thenReturn(null);

    // execute
    SpeakerModel result = sut.getSpeakerById(23L);

    // verify
    assertThat(result).isNull();
  }

  @Test
  public void getSpeakerById_findOne() {
    // prepare
    SpeakerModel oneSpeaker = new SpeakerModel();
    when(speakerRepo.findOne(23)).thenReturn(oneSpeaker);

    // execute
    SpeakerModel result = sut.getSpeakerById(23L);

    // verify
    assertThat(result).isSameAs(oneSpeaker);
  }

  @Test
  public void saveSpeaker_invalidParameter() {
    // verify
    expectedException(IllegalArgumentException.class, "speaker was null");

    // execute
    sut.saveSpeaker(null);
  }

  @Test
  public void saveSpeaker_saveNew() {
    // prepare
    SpeakerModel oneSpeaker = new SpeakerModel();

    // execute
    sut.saveSpeaker(oneSpeaker);

    // verify
    ArgumentCaptor<SpeakerModel> savedSpeaker = ArgumentCaptor.forClass(SpeakerModel.class);
    verify(speakerRepo).saveAndFlush(savedSpeaker.capture());
    assertThat(savedSpeaker.getValue().getCreatedAt()).isNotNull();
  }
  
  @Test
  public void saveSpeaker_saveExisting() {
    // prepare
    SpeakerModel oneSpeaker = new SpeakerModel();
    oneSpeaker.setId(23);
    
    // execute
    sut.saveSpeaker(oneSpeaker);
    
    // verify
    ArgumentCaptor<SpeakerModel> savedSpeaker = ArgumentCaptor.forClass(SpeakerModel.class);
    verify(speakerRepo).saveAndFlush(savedSpeaker.capture());
    assertThat(savedSpeaker.getValue().getCreatedAt()).isNull();
  }

}
