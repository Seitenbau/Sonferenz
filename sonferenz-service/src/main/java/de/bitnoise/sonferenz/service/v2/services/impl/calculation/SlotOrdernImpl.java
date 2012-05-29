package de.bitnoise.sonferenz.service.v2.services.impl.calculation;

import java.util.List;

import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.SlotOrdern;

public class SlotOrdernImpl implements SlotOrdern
{

  List<SlotReference> _result;
  Integer _quality;

  public SlotOrdernImpl(List<SlotReference> result, Integer quality)
  {
    _result = result;
    _quality=quality;
  }

  public List<SlotReference> getResult()
  {
    return _result;
  }

  @Override
  public Integer getQuality()
  {
    return _quality;
  }

}
