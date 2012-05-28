package de.bitnoise.sonferenz.service.v2.services.impl.calculation;

import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.Collisions;

class CollisionsStat implements Collisions {

  int _strength;

  public CollisionsStat(int strength) {
    _strength = strength;
  }

  @Override
  public boolean isPossible() {
    return _strength >= 0;
  }

  @Override
  public int getStrength() {
    return _strength;
  }

  @Override
  public String toString() {
    return Integer.toString(_strength);
  }

}