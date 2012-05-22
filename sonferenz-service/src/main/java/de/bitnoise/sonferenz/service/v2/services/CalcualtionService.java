package de.bitnoise.sonferenz.service.v2.services;

import java.util.List;
import java.util.Set;


import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.model.ProposalModel;
import de.bitnoise.sonferenz.service.v2.exceptions.GeneralConferenceException;
import de.bitnoise.sonferenz.service.v2.extend.ports.TimeTable;

public interface CalcualtionService
{

  TimeTable getCurrentList(ConferenceModel conference)
      throws GeneralConferenceException;

}
