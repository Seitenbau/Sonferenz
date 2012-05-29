package de.bitnoise.sonferenz.service.v2.services.impl.calculation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CalcTalk;

public class SlotReference
{
  List<SlotItem> _refs = new ArrayList<SlotItem>();

  List<CalcTalk> _talks = new ArrayList<CalcTalk>();

  public void add(OneSlot slot)
  {
    _refs.add(slot);
  }

  public List<SlotItem> getAll()
  {
    return _refs;
  }

  public SlotItem get(int i)
  {
    return _refs.get(i);
  }

  public void add(SlotReference sut)
  {
    _refs.addAll(sut.getAll());
  }

  public void add(CalcTalk vortrag)
  {
    _talks.add(vortrag);
  }
  
  public CalcTalk getTalk(int i ) {
    return _talks.get(i);
  }

  public List<CalcTalk> getTalks()
  {
    return _talks;
  }
  
}