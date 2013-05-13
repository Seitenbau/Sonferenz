package de.bitnoise.sonferenz.service.v2.services.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.internal.verification.NoMoreInteractions;


import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.repo.ConferenceRepository;
import de.bitnoise.sonferenz.service.v2.BaseTestClass;
import de.bitnoise.sonferenz.service.v2.exceptions.GeneralConferenceException;
import de.bitnoise.sonferenz.service.v2.services.impl.ConferenceService2Impl;

public class ConferenceFacadeImplTest extends BaseTestClass
{
  ConferenceRepository conferenceRepoMock = mock(ConferenceRepository.class);
  ConferenceService2Impl sut = new ConferenceService2Impl() {
    {
      conferenceRepo = conferenceRepoMock;
    }
  };

  ConferenceModel _current = new ConferenceModel();

  @Test
  public void testGetActiveConference_One()
  {
    // prepare
    when(conferenceRepoMock.findByActive(anyBoolean())).thenReturn(_current);

    // execute
    ConferenceModel active = sut.getActiveConference2();

    // verify
    assertThat(active).isEqualTo(_current);
  }

  @Test
  public void testGetActiveConference_None()
  {
    // prepare
    when(conferenceRepoMock.findByActive(anyBoolean())).thenReturn(null);

    // execute
    ConferenceModel active = sut.getActiveConference2();

    // verify
    assertThat(active).isNull();
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testGetActiveConference_Fail()
  {
    // prepare
    when(conferenceRepoMock.findByActive(anyBoolean()))
                .thenThrow(RuntimeException.class);

    // expect a exception
    expectException.expect(GeneralConferenceException.class);

    // execute
    sut.getActiveConference2();
  }

}
