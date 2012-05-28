package de.bitnoise.sonferenz.service.v2.services.impl.calculation;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CalcTalk;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CollisionResult;

public class CalculateSlots {

  protected CalculationConfigurationImpl _config;

  public CalculateSlots(CalculationConfigurationImpl theConfig) {
    _config = theConfig;
  }

  public SlotOrdernImpl calculate(CollisionResult collisions) {
    SlotOrdernImpl x = new SlotOrdernImpl();

    int talkCount = _config.getTalks().size();

    ConferenceSlots days = _config.getSlotConfig();

    SlotReference day1 = new SlotReference();
    days.addParallel("Fr 15:30", 2);
    days.addParallel("Fr 17:00", 2);

    days.addParallel("Fr 10:00", 2);
    days.addParallel("Fr 11:30", 2);
    days.addParallel("Fr 13:00", 2);
    days.addParallel("Fr 14:30", 2);

    return null;
  }

}
