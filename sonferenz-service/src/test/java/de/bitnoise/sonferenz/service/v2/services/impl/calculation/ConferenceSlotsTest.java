package de.bitnoise.sonferenz.service.v2.services.impl.calculation;

import java.util.List;

import org.junit.Test;


import static org.fest.assertions.Assertions.*;

public class ConferenceSlotsTest {

  ConferenceSlots sut = new ConferenceSlots();

  @Test
  public void testEmpty() {
    assertThat(sut.getAllSlots()).isEmpty();
  }

  @Test
  public void addParallel() {
    // execute
    SlotReference ref1 = sut.addParallel("Fr 15:30", 2);
    SlotReference ref2 = sut.addParallel("Fr 17:00", 3);

    // verify counts
    List<SlotItem> allSlots = sut.getAllSlots();
    assertThat(allSlots).hasSize(5);
    assertThat(ref1.getAll()).hasSize(2);
    assertThat(ref2.getAll()).hasSize(3);

    SlotItem s1t1 = ref1.getAll().get(0);
    SlotItem s1t2 = ref1.getAll().get(1);
    SlotItem s2t1 = ref2.getAll().get(0);
    SlotItem s2t2 = ref2.getAll().get(1);
    SlotItem s2t3 = ref2.getAll().get(2);

    assertThat(allSlots.get(0)).isSameAs(s1t1);
    assertThat(allSlots.get(1)).isSameAs(s1t2);
    assertThat(allSlots.get(2)).isSameAs(s2t1);
    assertThat(allSlots.get(3)).isSameAs(s2t2);
    assertThat(allSlots.get(4)).isSameAs(s2t3);
  }
  
  @Test
  public void addParallelAndSlotReferences() {
    // execute
    SlotReference ref1 = sut.addParallel("Fr 15:30", 2);
    SlotReference ref2 = sut.addParallel("Fr 17:00", 3);
    
    // verify counts
    List<SlotItem> allSlots = sut.getAllSlots();
    assertThat(allSlots).hasSize(5);
    assertThat(ref1.getAll()).hasSize(2);
    assertThat(ref2.getAll()).hasSize(3);
    
    SlotItem s1t1 = ref1.getAll().get(0);
    SlotItem s1t2 = ref1.getAll().get(1);
    SlotItem s2t1 = ref2.getAll().get(0);
    SlotItem s2t2 = ref2.getAll().get(1);
    SlotItem s2t3 = ref2.getAll().get(2);
    
    assertThat(allSlots.get(0)).isSameAs(s1t1);
    assertThat(allSlots.get(1)).isSameAs(s1t2);
    assertThat(allSlots.get(2)).isSameAs(s2t1);
    assertThat(allSlots.get(3)).isSameAs(s2t2);
    assertThat(allSlots.get(4)).isSameAs(s2t3);
  }

}
