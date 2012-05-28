package de.bitnoise.sonferenz.service.v2.services.impl.calculation;

import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CalcUser;

public class CalculationUserImpl implements CalcUser {
  protected Object _referenceId;

  public CalculationUserImpl(Object reference) {
    _referenceId = reference;
  }

  @Override
  public Object getId() {
    return _referenceId;
  }
  
  @Override
  public String toString() {
    return _referenceId.toString();
  }

}