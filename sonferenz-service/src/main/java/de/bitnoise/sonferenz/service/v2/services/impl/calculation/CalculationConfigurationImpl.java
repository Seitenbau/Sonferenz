package de.bitnoise.sonferenz.service.v2.services.impl.calculation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CalculationConfiguration;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CalculationConstraint;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CalcTalk;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CalcUser;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.ConstraintFunction;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.Slot;

public class CalculationConfigurationImpl implements CalculationConfiguration {

  protected ConferenceSlots  _slots = new ConferenceSlots();
  protected List<CalculationUserImpl> _users = new ArrayList<CalculationUserImpl>();
  protected List<CalculationTalkImpl> _talks = new ArrayList<CalculationTalkImpl>();

  @Override
  public SlotReference createSlot(Object reference, Integer parallelCount) {
    return _slots.addParallel(reference.toString(), parallelCount);
  }

  @Override
  public CalcUser addUser(Object reference) {
    CalculationUserImpl user = new CalculationUserImpl(reference);
    _users.add(user);
    return user;
  }

  @Override
  public CalcTalk addTalk(Object reference) {
    CalculationTalkImpl talk = new CalculationTalkImpl(reference);
    _talks.add(talk);
    return talk;
  }

  public ConferenceSlots getSlotConfig() {
    return _slots;
  }
  
  public List<CalculationUserImpl> getUsers() {
    return _users;
  }

  public List<CalculationTalkImpl> getTalks() {
    return _talks;
  }

}