package de.bitnoise.sonferenz.service.v2.services.impl.calculation;

import java.util.List;

import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CalculationConfiguration;

public class CalculateTimeTableServiceImpl implements CalculateTimetableService {

  @Override
  public CalculationConfiguration createConfig() {
    return new CalculationConfigurationImpl();
  }

  @Override
  public CollisionResult calculateCollisions(CalculationConfiguration config) {

    CalculationConfigurationImpl theConfig = verifyInput(config);

    CalculatorLogic logic = new CalculatorLogic(theConfig);

    CollisionResult state = logic.calculateCollisions();

    return state;
  }

  @Override
  public SlotOrdern calculateTalkorder(CalculationConfiguration config, CollisionResult collisions) {
    CalculationConfigurationImpl theConfig = verifyInput(config);
    CalculateSlots logic = new CalculateSlots(theConfig);
    SlotOrdernImpl state = logic.calculate(collisions);
    return state;
  }

  protected CalculationConfigurationImpl verifyInput(CalculationConfiguration config) {
    if (!(config instanceof CalculationConfigurationImpl)) {
      throw new IllegalArgumentException("Config of invalid type. user createConfig() to create the correct type");
    }
    CalculationConfigurationImpl theConfig = (CalculationConfigurationImpl) config;

    List<SlotItem> slots = theConfig.getSlotConfig().getAllSlots();

    if (slots.size() != theConfig.getTalks().size()) {
      throw new IllegalStateException("The count of slots is not the same as the talks count");
    }

    return theConfig;
  }

}
