package de.bitnoise.sonferenz.service.v2.services.impl.calculation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class ConferenceSlots {

  protected List<SlotItem> _allslots = new ArrayList<SlotItem>();

  public SlotReference addParallel(String name, int talksInParallel) {
    SlotReference ref = new SlotReference();
    for (int i = 0; i < talksInParallel; i++) {
      OneSlot slot = new OneSlot(name + "-" + i);
      ref.add(slot);
      _allslots.add(slot);
    }
    return ref;
  }

  public List<SlotItem> getAllSlots() {
    return _allslots;
  }

}
