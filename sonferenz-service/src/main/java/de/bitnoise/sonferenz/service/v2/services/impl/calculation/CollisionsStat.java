package de.bitnoise.sonferenz.service.v2.services.impl.calculation;

import java.util.ArrayList;
import java.util.List;

import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.Collisions;

class CollisionsStat implements Collisions
{

  int _strength;

  List<ConstraintEvent> _events = new ArrayList<ConstraintEvent>();

  public CollisionsStat(int strength)
  {
    _strength = strength;
  }

  public CollisionsStat(int strength, List<ConstraintEvent> events)
  {
    _strength = strength;
    _events.addAll(events);
  }

  @Override
  public boolean isPossible()
  {
    return _strength >= 0;
  }

  @Override
  public int getStrength()
  {
    return _strength;
  }

  @Override
  public String toString()
  {
    return Integer.toString(_strength);
  }

  @Override
  public List<ConstraintEvent> getConstrainingEvents()
  {
    return _events;
  }

}