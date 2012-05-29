package de.bitnoise.sonferenz.service.v2.services.impl.calculation;

import static org.fest.assertions.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import de.bitnoise.sonferenz.service.v2.BaseTestClass;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CalcTalk;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CalcUser;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CollisionResult;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.Collisions;
import de.bitnoise.testing.mockito.MockitoRule;

public class CalculateSlotsTest extends BaseTestClass
{
  CalculationConfigurationImpl config = new CalculationConfigurationImpl();

  CalculateSlots sut = new CalculateSlots(config);

  @Test
  public void test2slots2talks()
  {
    SlotReference slot1 = config.createSlot("slot1", 2);

    config.addTalk("t1");
    config.addTalk("t2");

    List<CalculationTalkImpl> talks = new ArrayList<CalculationTalkImpl>();
    CollisionResult collisions = new CollisionResult()
    {
      @Override
      public Collisions getCollisions(CalcTalk talk1, CalcTalk talk2)
      {
        return null;
      }
    };

    List<SlotReference> sl = config.getSlotConfig().getAllSlots();
    List<SlotReference> result = sut.createPair(collisions, sl,
        config.getTalks());

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getTalks()).hasSize(2);
    assertThat(result.get(0).getTalk(0).toString()).isEqualTo("t2");
    assertThat(result.get(0).getTalk(1).toString()).isEqualTo("t1");
  }

  @Test
  public void test4slots4talks()
  {
    config.createSlot("slot1", 2);
    config.createSlot("slot2", 2);

    config.addTalk("t1");
    config.addTalk("t2");
    config.addTalk("t3");
    config.addTalk("t4");

    List<CalculationTalkImpl> talks = new ArrayList<CalculationTalkImpl>();
    CollisionResult collisions = new CollisionResult()
    {
      @Override
      public Collisions getCollisions(CalcTalk talk1, CalcTalk talk2)
      {
        return null;
      }
    };

    List<SlotReference> sl = config.getSlotConfig().getAllSlots();
    List<SlotReference> result = sut.createPair(collisions, sl,
        config.getTalks());

    assertThat(result).hasSize(2);
    assertThat(result.get(0).getTalks()).hasSize(2);
    assertThat(result.get(0).getTalk(0).toString()).isEqualTo("t4");
    assertThat(result.get(0).getTalk(1).toString()).isEqualTo("t3");
    assertThat(result.get(1).getTalks()).hasSize(2);
    assertThat(result.get(1).getTalk(0).toString()).isEqualTo("t2");
    assertThat(result.get(1).getTalk(1).toString()).isEqualTo("t1");
  }

  @Test
  public void test4slots4talksWithConstrain()
  {
    config.createSlot("slot1", 2);
    config.createSlot("slot2", 2);

    CalcUser u1 = config.addUser("u1");

    CalcTalk t1 = config.addTalk("t1");
    CalcTalk t2 = config.addTalk("t2");
    CalcTalk t3 = config.addTalk("t3");
    CalcTalk t4 = config.addTalk("t4");

    t1.addVisit(1, u1);
    t2.addVisit(1, u1);

    CollisionResult collisions = new CalculatorLogic(config)
        .calculateCollisions();

    List<SlotReference> sl = config.getSlotConfig().getAllSlots();
    List<SlotReference> result = sut.createPair(collisions, sl,
        config.getTalks());

    assertThat(result).hasSize(2);
    assertThat(result.get(0).getTalks()).hasSize(2);
    assertThat(result.get(0).getTalk(0).toString()).isEqualTo("t4");
    assertThat(result.get(0).getTalk(1).toString()).isEqualTo("t2");
    assertThat(result.get(1).getTalks()).hasSize(2);
    assertThat(result.get(1).getTalk(0).toString()).isEqualTo("t3");
    assertThat(result.get(1).getTalk(1).toString()).isEqualTo("t1");
  }

  @Test
  public void test4slots4talksWithSpeaker()
  {
    config.createSlot("slot1", 2);
    config.createSlot("slot2", 2);

    CalcUser u1 = config.addUser("u1");

    CalcTalk t1 = config.addTalk("t1");
    CalcTalk t2 = config.addTalk("t2");
    CalcTalk t3 = config.addTalk("t3");
    CalcTalk t4 = config.addTalk("t4");

    t1.addSpeaker(u1);
    t2.addSpeaker(u1);

    CollisionResult collisions = new CalculatorLogic(config)
        .calculateCollisions();

    List<SlotReference> sl = config.getSlotConfig().getAllSlots();
    List<SlotReference> result = sut.createPair(collisions, sl,
        config.getTalks());

    assertThat(result).hasSize(2);
    assertThat(result.get(0).getTalks()).hasSize(2);
    assertThat(result.get(0).getTalk(0).toString()).isEqualTo("t4");
    assertThat(result.get(0).getTalk(1).toString()).isEqualTo("t2");
    assertThat(result.get(1).getTalks()).hasSize(2);
    assertThat(result.get(1).getTalk(0).toString()).isEqualTo("t3");
    assertThat(result.get(1).getTalk(1).toString()).isEqualTo("t1");
  }

  @Test
  public void test4slots4talks_NotSolvable1Constraint()
  {
    config.createSlot("slot1", 2);
    config.createSlot("slot2", 2);

    CalcUser u1 = config.addUser("u1");
    CalcUser u2 = config.addUser("u2");

    CalcTalk t1 = config.addTalk("t1");
    CalcTalk t2 = config.addTalk("t2");
    CalcTalk t3 = config.addTalk("t3");
    CalcTalk t4 = config.addTalk("t4");

    t3.addVisit(1, u2);
    t4.addVisit(1, u2);
    t1.addVisit(1, u2);
    t2.addVisit(1, u2);

    CollisionResult collisions = new CalculatorLogic(config)
        .calculateCollisions();

    List<SlotReference> sl = config.getSlotConfig().getAllSlots();
    List<SlotReference> result = sut.createPair(collisions, sl,
        config.getTalks());

    assertThat(result).hasSize(2);
    assertThat(result.get(0).getTalks()).hasSize(2);
    assertThat(result.get(0).getTalk(0).toString()).isEqualTo("t4");
    assertThat(result.get(0).getTalk(1).toString()).isEqualTo("t3");
    assertThat(result.get(1).getTalks()).hasSize(2);
    assertThat(result.get(1).getTalk(0).toString()).isEqualTo("t2");
    assertThat(result.get(1).getTalk(1).toString()).isEqualTo("t1");
  }

  @Test
  public void test4slots4talks_SolvableConstraint()
  {
    config.createSlot("slot1", 2);
    config.createSlot("slot2", 2);

    CalcUser u1 = config.addUser("u1");
    CalcUser u2 = config.addUser("u2");
    CalcUser u3 = config.addUser("u3");
    CalcUser u4 = config.addUser("u4");

    CalcTalk t1 = config.addTalk("t1");
    CalcTalk t2 = config.addTalk("t2");
    CalcTalk t3 = config.addTalk("t3");
    CalcTalk t4 = config.addTalk("t4");

    t1.addSpeaker(u1);
    t2.addSpeaker(u2);
    t3.addSpeaker(u3);
    t4.addSpeaker(u4);

    t1.addVisit(1, u2);
    t3.addVisit(10, u2);
    t4.addVisit(1, u2);

    CollisionResult collisions = new CalculatorLogic(config)
        .calculateCollisions();

    List<SlotReference> sl = config.getSlotConfig().getAllSlots();
    List<SlotReference> result = sut.createPair(collisions, sl,
        config.getTalks());

    assertThat(result).hasSize(2);
    assertThat(result.get(0).getTalks()).hasSize(2);
    assertThat(result.get(0).getTalk(0).toString()).isEqualTo("t4");
    assertThat(result.get(0).getTalk(1).toString()).isEqualTo("t3");
    assertThat(result.get(1).getTalks()).hasSize(2);
    assertThat(result.get(1).getTalk(0).toString()).isEqualTo("t2");
    assertThat(result.get(1).getTalk(1).toString()).isEqualTo("t1");
  }

  @Test
  public void test4slots4talks_SolvableConstraintButSpeaker()
  {
    config.createSlot("slot1", 2);
    config.createSlot("slot2", 2);

    CalcUser u1 = config.addUser("u1");
    CalcUser u2 = config.addUser("u2");
    CalcUser u3 = config.addUser("u3");
    CalcUser u4 = config.addUser("u4");

    CalcTalk t1 = config.addTalk("t1");
    CalcTalk t2 = config.addTalk("t2");
    CalcTalk t3 = config.addTalk("t3");
    CalcTalk t4 = config.addTalk("t4");

    t1.addSpeaker(u1);
    t2.addSpeaker(u2);
    t3.addSpeaker(u3);
    t4.addSpeaker(u4);

    t3.addSpeaker(u2);
    t4.addVisit(1, u3);

    CollisionResult collisions = new CalculatorLogic(config)
        .calculateCollisions();
    System.out.println(collisions);
    List<SlotReference> sl = config.getSlotConfig().getAllSlots();
    List<SlotReference> result = sut.createPair(collisions, sl,
        config.getTalks());

    assertThat(result).hasSize(2);
    assertThat(result.get(0).getTalks()).hasSize(2);
    assertThat(result.get(0).getTalk(0).toString()).isEqualTo("t4");
    assertThat(result.get(0).getTalk(1).toString()).isEqualTo("t2");
    assertThat(result.get(1).getTalks()).hasSize(2);
    assertThat(result.get(1).getTalk(0).toString()).isEqualTo("t3");
    assertThat(result.get(1).getTalk(1).toString()).isEqualTo("t1");
  }
  @Test
  @Ignore // 4me
  public void longrun()
  {
    config.createSlot("slot1", 2);
    config.createSlot("slot2", 2);
    
    config.createSlot("slot3", 2);
    config.createSlot("slot4", 2);
    config.createSlot("slot5", 2);
    config.createSlot("slot6", 2);
    
    CalcUser u1 = config.addUser("u1");
    CalcUser u2 = config.addUser("u2");
    CalcUser u3 = config.addUser("u3");
    CalcUser u4 = config.addUser("u4");
    CalcUser u5 = config.addUser("u5");
    CalcUser u6 = config.addUser("u6");
    CalcUser u7 = config.addUser("u7");
    CalcUser u8 = config.addUser("u8");
    CalcUser u9 = config.addUser("u9");
    CalcUser u10 = config.addUser("u10");
    CalcUser u11 = config.addUser("u11");
    CalcUser u12 = config.addUser("u12");
    
    CalcTalk t1 = config.addTalk("t1");
    CalcTalk t2 = config.addTalk("t2");
    CalcTalk t3 = config.addTalk("t3");
    CalcTalk t4 = config.addTalk("t4");
    CalcTalk t5 = config.addTalk("t5");
    CalcTalk t6 = config.addTalk("t6");
    CalcTalk t7 = config.addTalk("t7");
    CalcTalk t8 = config.addTalk("t8");
    CalcTalk t9 = config.addTalk("t9");
    CalcTalk t10 = config.addTalk("t10");
    CalcTalk t11 = config.addTalk("t11");
    CalcTalk t12 = config.addTalk("t12");
    
    t1.addSpeaker(u1);
    t2.addSpeaker(u2);
    t3.addSpeaker(u3);
    t4.addSpeaker(u4);
    t5.addSpeaker(u5);
    t6.addSpeaker(u6);
    t7.addSpeaker(u7);
    t8.addSpeaker(u8);
    t9.addSpeaker(u9);
    t10.addSpeaker(u10);
    t11.addSpeaker(u11);
    t12.addSpeaker(u12);
    
    CollisionResult collisions = new CalculatorLogic(config)
    .calculateCollisions();
    System.out.println(collisions);
    List<SlotReference> sl = config.getSlotConfig().getAllSlots();
    List<SlotReference> result = sut.createPair(collisions, sl,
        config.getTalks());
    
    assertThat(result).hasSize(2);
    assertThat(result.get(0).getTalks()).hasSize(2);
    assertThat(result.get(0).getTalk(0).toString()).isEqualTo("t4");
    assertThat(result.get(0).getTalk(1).toString()).isEqualTo("t2");
    assertThat(result.get(1).getTalks()).hasSize(2);
    assertThat(result.get(1).getTalk(0).toString()).isEqualTo("t3");
    assertThat(result.get(1).getTalk(1).toString()).isEqualTo("t1");
  }
}
