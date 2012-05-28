package de.bitnoise.sonferenz.service.v2.services.impl.calculation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SlotReference {
  List<SlotItem> _refs = new ArrayList<SlotItem>();

  public void add(OneSlot slot) {
    _refs.add(slot);
  }

  public List<SlotItem> getAll() {
    return _refs;
  }

  public SlotItem get(int i) {
    return _refs.get(i);
  }

  public void add(SlotReference sut) {
    _refs.addAll(sut.getAll());
  }
}