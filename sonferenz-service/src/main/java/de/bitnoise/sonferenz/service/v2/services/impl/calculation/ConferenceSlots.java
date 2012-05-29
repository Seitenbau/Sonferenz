package de.bitnoise.sonferenz.service.v2.services.impl.calculation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class ConferenceSlots {

  protected List<SlotReference> _allslots = new ArrayList<SlotReference>();

  public SlotReference addParallel(String name, int talksInParallel) {
    SlotReference ref = new SlotReference();
    for (int i = 0; i < talksInParallel; i++) {
      OneSlot slot = new OneSlot(name + "-" + i);
      ref.add(slot);
    }
    _allslots.add(ref);
    return ref;
  }

  public List<SlotReference> getAllSlots() {
    return _allslots;
  }

}
