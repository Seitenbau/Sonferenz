package de.bitnoise.sonferenz.service.v2.services.impl.calculation;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

public class SlotReferenceTest {
  SlotReference sut = new SlotReference();

  @Test
  public void empty() {
    assertThat(sut.getAll()).isEmpty();
  }

  @Test
  public void addOne() {
    // prepare
    OneSlot slot1 = new OneSlot("1");
    // execute
    sut.add(slot1);
    // verify
    assertThat(sut.getAll()).hasSize(1);
    assertThat(sut.get(0)).isSameAs(slot1);
  }

  @Test
  public void addTwo() {
    // prepare
    OneSlot slot1 = new OneSlot("1");
    OneSlot slot2 = new OneSlot("2");
    // execute
    sut.add(slot2);
    sut.add(slot1);
    // verify
    assertThat(sut.getAll()).hasSize(2);
    assertThat(sut.get(0)).isSameAs(slot2);
    assertThat(sut.get(1)).isSameAs(slot1);
  }

  @Test
  public void addTwoRefs() {
    // prepare
    OneSlot slot1 = new OneSlot("1");
    OneSlot slot2 = new OneSlot("2");
    OneSlot slot3 = new OneSlot("3");
    OneSlot slot4 = new OneSlot("4");
    // execute
    SlotReference sut2 = new SlotReference();
    sut2.add(slot3);

    sut.add(slot2);
    sut.add(slot1);
    sut2.add(sut);

    // should not be added to sut2!
    sut.add(slot4);
    // verify sut2
    assertThat(sut2.getAll()).hasSize(3);
    assertThat(sut2.get(0)).isSameAs(slot3);
    assertThat(sut2.get(1)).isSameAs(slot2);
    assertThat(sut2.get(2)).isSameAs(slot1);
    
    // verify sut
    assertThat(sut.getAll()).hasSize(3);
    assertThat(sut.get(0)).isSameAs(slot2);
    assertThat(sut.get(1)).isSameAs(slot1);
    assertThat(sut.get(2)).isSameAs(slot4);
  }
}
