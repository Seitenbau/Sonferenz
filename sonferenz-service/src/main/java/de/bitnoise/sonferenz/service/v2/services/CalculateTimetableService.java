package de.bitnoise.sonferenz.service.v2.services;

import de.bitnoise.sonferenz.service.v2.services.impl.calculation.CalculationTalkImpl;
import de.bitnoise.sonferenz.service.v2.services.impl.calculation.SlotReference;

public interface CalculateTimetableService {

  CalculationConfiguration createConfig();

  CollisionResult calculateCollisions(CalculationConfiguration config);

  SlotOrdern calculateTalkorder(CalculationConfiguration config, CollisionResult collisions);

  interface CalculationConfiguration {
    CalcUser addUser(Object reference);

    CalcTalk addTalk(Object reference);

    SlotReference createSlot(Object reference, Integer parallelCount);

  }

  public interface Slot {

    Integer getRooms();

    RoomAtPointInTime getRoom(int i);
  }

  public interface SlotOrdern {

  }

  public interface RoomAtPointInTime {

  }

  public interface CalcTalk {
    Object getId();

    void addConstraint(ConstraintFunction func, CalcUser user);
  }

  public interface CalcUser {
    Object getId();
  }

  public interface ConstraintFunction {

    Integer calculate(CalculationTalkImpl talkB, CalcUser _user);

  }

  public interface CalculationConstraint {

  }

  public interface CollisionResult {

    Collisions getCollisions(CalcTalk talk1, CalcTalk talk2);

  }

  public interface Collisions {

    boolean isPossible();

    int getStrength();

  }

}
