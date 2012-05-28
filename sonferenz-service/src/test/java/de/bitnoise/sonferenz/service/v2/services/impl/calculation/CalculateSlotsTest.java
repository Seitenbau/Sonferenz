package de.bitnoise.sonferenz.service.v2.services.impl.calculation;

import static org.fest.assertions.Assertions.*;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import de.bitnoise.sonferenz.service.v2.BaseTestClass;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CollisionResult;
import de.bitnoise.testing.mockito.MockitoRule;

public class CalculateSlotsTest extends BaseTestClass {
  CalculationConfigurationImpl config = new CalculationConfigurationImpl();
  CalculateSlots sut = new CalculateSlots(config);
  @Mock
  CollisionResult collisions;

  @Test
  public void test1() {
    SlotReference slot1 = config.createSlot("slot1", 2);
    SlotReference slot2 = config.createSlot("slot1", 2);

    SlotOrdernImpl result = sut.calculate(collisions);
  }
}
