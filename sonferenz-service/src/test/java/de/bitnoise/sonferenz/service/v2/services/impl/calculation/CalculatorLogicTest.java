package de.bitnoise.sonferenz.service.v2.services.impl.calculation;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CalcTalk;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CalcUser;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CollisionResult;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.Collisions;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.ConstraintFunction;
import de.bitnoise.sonferenz.service.v2.services.impl.calculation.CalculatorLogicTest.ResultCheck.TalkCheck;

public class CalculatorLogicTest {

  CalculationConfigurationImpl config = new CalculationConfigurationImpl();
  CalculatorLogic sut = new CalculatorLogic(config);
  
  ConstraintFunction speaker=ConstraintFuncs.speaker();
  ConstraintFunction visit=ConstraintFuncs.visit(1);

  @Test
  public void afterInit() {
    // prepare
    CalcTalk talk1 = config.addTalk("t1");
    CalcTalk talk2 = config.addTalk("t2");
    CalcTalk talk3 = config.addTalk("t3");
    CalcTalk talk4 = config.addTalk("t4");

    // execute
    CollisionResult result = sut.calculateCollisions();

    // verify
    check(result).talk(talk1, talk1).isImpossible();
    check(result).talk(talk1, talk2).isPossible(0);
    check(result).talk(talk1, talk3).isPossible(0);
    check(result).talk(talk1, talk4).isPossible(0);
    check(result).talk(talk2, talk1).isPossible(0);
    check(result).talk(talk2, talk2).isImpossible();
    check(result).talk(talk2, talk3).isPossible(0);
    check(result).talk(talk2, talk4).isPossible(0);
    check(result).talk(talk3, talk1).isPossible(0);
    check(result).talk(talk3, talk2).isPossible(0);
    check(result).talk(talk3, talk3).isImpossible();
    check(result).talk(talk3, talk4).isPossible(0);
    check(result).talk(talk4, talk1).isPossible(0);
    check(result).talk(talk4, talk2).isPossible(0);
    check(result).talk(talk4, talk3).isPossible(0);
    check(result).talk(talk4, talk4).isImpossible();
  }

  @Test
  public void withSpeakersConstraints() {
    // prepare
    CalcTalk talk1 = config.addTalk("t1");
    CalcTalk talk2 = config.addTalk("t2");
    CalcTalk talk3 = config.addTalk("t3");
    CalcTalk talk4 = config.addTalk("t4");

    CalcUser user1 = config.addUser("u1");
    CalcUser user2 = config.addUser("u2");
    CalcUser user3 = config.addUser("u3");
    CalcUser user4 = config.addUser("u4");
    talk1.addSpeaker(  user1);
    talk2.addSpeaker( user2);
    talk3.addSpeaker( user3);
    talk4.addSpeaker( user4);

    // execute
    CollisionResult result = sut.calculateCollisions();

    // verify
    check(result).talk(talk1, talk1).isImpossible();
    check(result).talk(talk1, talk2).isPossible(0);
    check(result).talk(talk1, talk3).isPossible(0);
    check(result).talk(talk1, talk4).isPossible(0);
    check(result).talk(talk2, talk1).isPossible(0);
    check(result).talk(talk2, talk2).isImpossible();
    check(result).talk(talk2, talk3).isPossible(0);
    check(result).talk(talk2, talk4).isPossible(0);
    check(result).talk(talk3, talk1).isPossible(0);
    check(result).talk(talk3, talk2).isPossible(0);
    check(result).talk(talk3, talk3).isImpossible();
    check(result).talk(talk3, talk4).isPossible(0);
    check(result).talk(talk4, talk1).isPossible(0);
    check(result).talk(talk4, talk2).isPossible(0);
    check(result).talk(talk4, talk3).isPossible(0);
    check(result).talk(talk4, talk4).isImpossible();
  }

  @Test
  public void oneSpeakersConstraints() {
    // prepare
    CalcTalk talk1 = config.addTalk("t1");
    CalcTalk talk2 = config.addTalk("t2");
    CalcTalk talk3 = config.addTalk("t3");
    CalcTalk talk4 = config.addTalk("t4");

    CalcUser user1 = config.addUser("u1");
    CalcUser user2 = config.addUser("u2");
    CalcUser user3 = config.addUser("u3");
    CalcUser user4 = config.addUser("u4");
    talk1.addSpeaker( user1);
    talk1.addSpeaker( user2);
    talk2.addSpeaker( user2);
    talk3.addSpeaker( user3);
    talk4.addSpeaker( user4);

    // execute
    CollisionResult result = sut.calculateCollisions();

    // verify
    check(result).talk(talk1, talk1).isImpossible();
    check(result).talk(talk1, talk2).isImpossible();
    check(result).talk(talk1, talk3).isPossible(0);
    check(result).talk(talk1, talk4).isPossible(0);
    check(result).talk(talk2, talk1).isImpossible();
    check(result).talk(talk2, talk2).isImpossible();
    check(result).talk(talk2, talk3).isPossible(0);
    check(result).talk(talk2, talk4).isPossible(0);
    check(result).talk(talk3, talk1).isPossible(0);
    check(result).talk(talk3, talk2).isPossible(0);
    check(result).talk(talk3, talk3).isImpossible();
    check(result).talk(talk3, talk4).isPossible(0);
    check(result).talk(talk4, talk1).isPossible(0);
    check(result).talk(talk4, talk2).isPossible(0);
    check(result).talk(talk4, talk3).isPossible(0);
    check(result).talk(talk4, talk4).isImpossible();
  }

  @Test
  public void multipleSpeakersConstraints() {
    // prepare
    CalcTalk talk1 = config.addTalk("t1");
    CalcTalk talk2 = config.addTalk("t2");
    CalcTalk talk3 = config.addTalk("t3");
    CalcTalk talk4 = config.addTalk("t4");

    CalcUser user1 = config.addUser("u1");
    CalcUser user2 = config.addUser("u2");
    CalcUser user3 = config.addUser("u3");
    CalcUser user4 = config.addUser("u4");
    talk1.addSpeaker( user1);
    talk1.addSpeaker( user3);
    talk2.addSpeaker( user2);
    talk3.addSpeaker( user3);
    talk4.addSpeaker( user4);
    talk4.addSpeaker( user1);

    // execute
    CollisionResult result = sut.calculateCollisions();

    // verify
    check(result).talk(talk1, talk1).isImpossible();
    check(result).talk(talk1, talk2).isPossible(0);
    check(result).talk(talk1, talk3).isImpossible();
    check(result).talk(talk1, talk4).isImpossible();
    check(result).talk(talk2, talk1).isPossible(0);
    check(result).talk(talk2, talk2).isImpossible();
    check(result).talk(talk2, talk3).isPossible(0);
    check(result).talk(talk2, talk4).isPossible(0);
    check(result).talk(talk3, talk1).isImpossible();
    check(result).talk(talk3, talk2).isPossible(0);
    check(result).talk(talk3, talk3).isImpossible();
    check(result).talk(talk3, talk4).isPossible(0);
    check(result).talk(talk4, talk1).isImpossible();
    check(result).talk(talk4, talk2).isPossible(0);
    check(result).talk(talk4, talk3).isPossible(0);
    check(result).talk(talk4, talk4).isImpossible();
  }

  @Test
  public void multipleSpeakersConstraintsAndVisit() {
    // prepare
    CalcTalk talk1 = config.addTalk("t1");
    CalcTalk talk2 = config.addTalk("t2");
    CalcTalk talk3 = config.addTalk("t3");
    CalcTalk talk4 = config.addTalk("t4");

    CalcUser user1 = config.addUser("u1");
    CalcUser user2 = config.addUser("u2");
    CalcUser user3 = config.addUser("u3");
    CalcUser user4 = config.addUser("u4");

    talk1.addSpeaker( user1);
    talk1.addSpeaker( user3);
    talk2.addSpeaker( user2);
    talk3.addSpeaker( user3);
    talk4.addSpeaker( user4);

    talk4.addVisit(1, user1);

    // execute
    CollisionResult result = sut.calculateCollisions();

    // verify
    check(result).talk(talk1, talk1).isImpossible();
    check(result).talk(talk1, talk2).isPossible(0);
    check(result).talk(talk1, talk3).isImpossible();
    check(result).talk(talk1, talk4).print().isPossible(1);
    check(result).talk(talk2, talk1).isPossible(0);
    check(result).talk(talk2, talk2).isImpossible();
    check(result).talk(talk2, talk3).isPossible(0);
    check(result).talk(talk2, talk4).isPossible(0);
    check(result).talk(talk3, talk1).isImpossible();
    check(result).talk(talk3, talk2).isPossible(0);
    check(result).talk(talk3, talk3).isImpossible();
    check(result).talk(talk3, talk4).isPossible(0);
    check(result).talk(talk4, talk1).isPossible(1);
    check(result).talk(talk4, talk2).isPossible(0);
    check(result).talk(talk4, talk3).isPossible(0);
    check(result).talk(talk4, talk4).isImpossible();
  }

  @Test
  public void multipleSpeakersConstraintsAndmultipleVisits() {
    // prepare
    CalcTalk talk1 = config.addTalk("t1");
    CalcTalk talk2 = config.addTalk("t2");
    CalcTalk talk3 = config.addTalk("t3");
    CalcTalk talk4 = config.addTalk("t4");

    CalcUser user1 = config.addUser("u1");
    CalcUser user2 = config.addUser("u2");
    CalcUser user3 = config.addUser("u3");
    CalcUser user4 = config.addUser("u4");

    talk1.addSpeaker( user1);
    talk1.addSpeaker( user3);
    talk2.addSpeaker( user2);
    talk3.addSpeaker( user3);
    talk4.addSpeaker( user4);

    talk4.addVisit(1, user1);
    talk4.addVisit(1, user2);
    talk4.addVisit(1, user3);
    talk4.addVisit(1, user4);

    // execute
    CollisionResult result = sut.calculateCollisions();

    // verify
    check(result).talk(talk1, talk1).isImpossible();
    check(result).talk(talk1, talk2).isPossible(0);
    check(result).talk(talk1, talk3).isImpossible();
    check(result).talk(talk1, talk4).isPossible(2);
    check(result).talk(talk2, talk1).isPossible(0);
    check(result).talk(talk2, talk2).isImpossible();
    check(result).talk(talk2, talk3).isPossible(0);
    check(result).talk(talk2, talk4).isPossible(1);
    check(result).talk(talk3, talk1).isImpossible();
    check(result).talk(talk3, talk2).isPossible(0);
    check(result).talk(talk3, talk3).isImpossible();
    check(result).talk(talk3, talk4).isPossible(1);
    check(result).talk(talk4, talk1).isPossible(2);
    check(result).talk(talk4, talk2).isPossible(1);
    check(result).talk(talk4, talk3).isPossible(1);
    check(result).talk(talk4, talk4).isImpossible();
  }

  @Test
  public void multiConstraintRecalculation() {
    // prepare
    CalcTalk talk1 = config.addTalk("t1");
    CalcTalk talk2 = config.addTalk("t2");
    CalcTalk talk3 = config.addTalk("t3");
    CalcTalk talk4 = config.addTalk("t4");

    CalcUser user1 = config.addUser("u1");
    CalcUser user2 = config.addUser("u2");
    CalcUser user3 = config.addUser("u3");
    CalcUser user4 = config.addUser("u4");

    talk1.addSpeaker( user1);
    talk1.addSpeaker( user3);
    talk2.addSpeaker( user2);
    talk3.addSpeaker( user3);
    talk4.addSpeaker( user4);

    talk4.addVisit(1, user1);
    talk4.addVisit(1, user2);
    talk4.addVisit(1, user3);
    talk4.addVisit(1, user4);

    { // execute 1st time
      CollisionResult result = sut.calculateCollisions();

      // verify
      check(result).talk(talk1, talk1).isImpossible();
      check(result).talk(talk1, talk2).isPossible(0);
      check(result).talk(talk1, talk3).isImpossible();
      check(result).talk(talk1, talk4).isPossible(2);
      check(result).talk(talk2, talk1).isPossible(0);
      check(result).talk(talk2, talk2).isImpossible();
      check(result).talk(talk2, talk3).isPossible(0);
      check(result).talk(talk2, talk4).isPossible(1);
      check(result).talk(talk3, talk1).isImpossible();
      check(result).talk(talk3, talk2).isPossible(0);
      check(result).talk(talk3, talk3).isImpossible();
      check(result).talk(talk3, talk4).isPossible(1);
      check(result).talk(talk4, talk1).isPossible(2);
      check(result).talk(talk4, talk2).isPossible(1);
      check(result).talk(talk4, talk3).isPossible(1);
      check(result).talk(talk4, talk4).isImpossible();
    }
    { // execute 2st time
      CollisionResult result = sut.calculateCollisions();

      // verify
      check(result).talk(talk1, talk1).isImpossible();
      check(result).talk(talk1, talk2).isPossible(0);
      check(result).talk(talk1, talk3).isImpossible();
      check(result).talk(talk1, talk4).isPossible(2);
      check(result).talk(talk2, talk1).isPossible(0);
      check(result).talk(talk2, talk2).isImpossible();
      check(result).talk(talk2, talk3).isPossible(0);
      check(result).talk(talk2, talk4).isPossible(1);
      check(result).talk(talk3, talk1).isImpossible();
      check(result).talk(talk3, talk2).isPossible(0);
      check(result).talk(talk3, talk3).isImpossible();
      check(result).talk(talk3, talk4).isPossible(1);
      check(result).talk(talk4, talk1).isPossible(2);
      check(result).talk(talk4, talk2).isPossible(1);
      check(result).talk(talk4, talk3).isPossible(1);
      check(result).talk(talk4, talk4).isImpossible();
    }
    
    talk3.addVisit(1, user2);
    { // execute 3st time
      CollisionResult result = sut.calculateCollisions();
      
      // verify
      check(result).talk(talk1, talk1).isImpossible();
      check(result).talk(talk1, talk2).isPossible(0);
      check(result).talk(talk1, talk3).isImpossible();
      check(result).talk(talk1, talk4).isPossible(2);
      check(result).talk(talk2, talk1).isPossible(0);
      check(result).talk(talk2, talk2).isImpossible();
      check(result).talk(talk2, talk3).isPossible(1);
      check(result).talk(talk2, talk4).isPossible(1);
      check(result).talk(talk3, talk1).isImpossible();
      check(result).talk(talk3, talk2).isPossible(1);
      check(result).talk(talk3, talk3).isImpossible();
      check(result).talk(talk3, talk4).isPossible(2);
      check(result).talk(talk4, talk1).isPossible(2);
      check(result).talk(talk4, talk2).isPossible(1);
      check(result).talk(talk4, talk3).isPossible(2);
      check(result).talk(talk4, talk4).isImpossible();
    }
  }
  
  @Test
  public void visitWithDifferenWeight() {
    // prepare
    CalcTalk talk1 = config.addTalk("t1");
    CalcTalk talk2 = config.addTalk("t2");
    CalcTalk talk3 = config.addTalk("t3");
    CalcTalk talk4 = config.addTalk("t4");

    CalcUser user1 = config.addUser("u1");
    CalcUser user2 = config.addUser("u2");
    CalcUser user3 = config.addUser("u3");
    CalcUser user4 = config.addUser("u4");

    talk1.addSpeaker( user1);
    talk1.addSpeaker( user3);
    talk2.addSpeaker( user2);
    talk3.addSpeaker( user3);
    talk4.addSpeaker( user4);
 
    talk4.addVisit(1, user1);
    talk4.addVisit(2, user2);
    talk4.addVisit(3, user3);
    talk4.addVisit(4, user4);

    // execute
    CollisionResult result = sut.calculateCollisions();

    // verify
    check(result).talk(talk1, talk1).isImpossible();
    check(result).talk(talk1, talk2).isPossible(0);
    check(result).talk(talk1, talk3).isImpossible();
    check(result).talk(talk1, talk4).isPossible(1+3);
    check(result).talk(talk2, talk1).isPossible(0);
    check(result).talk(talk2, talk2).isImpossible();
    check(result).talk(talk2, talk3).isPossible(0);
    check(result).talk(talk2, talk4).isPossible(2);
    check(result).talk(talk3, talk1).isImpossible();
    check(result).talk(talk3, talk2).isPossible(0);
    check(result).talk(talk3, talk3).isImpossible();
    check(result).talk(talk3, talk4).isPossible(3);
    check(result).talk(talk4, talk1).isPossible(1+3);
    check(result).talk(talk4, talk2).isPossible(2);
    check(result).talk(talk4, talk3).isPossible(3);
    check(result).talk(talk4, talk4).isImpossible();
  }
  
  @Test
  public void visitWithSameWeightsOndDiff() {
    // prepare
    CalcTalk talk1 = config.addTalk("t1");
    CalcTalk talk2 = config.addTalk("t2");
    CalcTalk talk3 = config.addTalk("t3");
    CalcTalk talk4 = config.addTalk("t4");
    
    CalcUser user1 = config.addUser("u1");
    CalcUser user2 = config.addUser("u2");
    CalcUser user3 = config.addUser("u3");
    CalcUser user4 = config.addUser("u4");
    
    talk1.addVisit(1 , user1);
    talk2.addVisit(2, user1);
    talk3.addVisit(1, user1);
    talk4.addVisit(1, user1);
    
    // execute
    CollisionResult result = sut.calculateCollisions();
    System.out.println(result);
    
    // verify
    check(result).talk(talk1, talk1).isImpossible();
    check(result).talk(talk2, talk2).isImpossible();
    check(result).talk(talk3, talk3).isImpossible();
    check(result).talk(talk4, talk4).isImpossible();
    
    check(result).talk(talk1, talk2).isPossible(2);
    check(result).talk(talk1, talk3).isPossible(1);
    check(result).talk(talk1, talk4).isPossible(1);
    check(result).talk(talk2, talk1).isPossible(2);
    check(result).talk(talk2, talk3).isPossible(1);
    check(result).talk(talk2, talk4).isPossible(1);
    check(result).talk(talk3, talk1).isPossible(1);
    check(result).talk(talk3, talk2).isPossible(1);
    check(result).talk(talk3, talk4).isPossible(1);
    check(result).talk(talk4, talk1).isPossible(1);
    check(result).talk(talk4, talk2).isPossible(1);
    check(result).talk(talk4, talk3).isPossible(1);
  }

  // ***************************************************************
  // ********************* Helpers *********************************
  // ***************************************************************

  ResultCheck check(CollisionResult result) {
    return new ResultCheck(result);
  }

  class ResultCheck {

    CollisionResult _result;

    public ResultCheck(CollisionResult result) {
      _result = result;
    }

    public TalkCheck talk(CalcTalk talk1, CalcTalk talk2) {
      assertThat(_result).isNotNull();
      return new TalkCheck(_result.getCollisions(talk1, talk2));
    }

    class TalkCheck {

      Collisions _collision;

      public TalkCheck(Collisions collisions) {
        _collision = collisions;
      }

      public TalkCheck print()
      {
        for(ConstraintEvent e : _collision.getConstrainingEvents()) {
          System.out.println(e);
        }
        return this;
      }

      public void isPossible(int i) {
        isNotNull();
        assertThat(_collision.isPossible()).isTrue();
        assertThat(_collision.getStrength()).isEqualTo(i);
      }

      public void isImpossible() {
        isNotNull();
        assertThat(_collision.getStrength()).isEqualTo(-1);
        assertThat(_collision.isPossible()).isFalse();
      }

      public void isNotNull() {
        assertThat(_collision).isNotNull();
      }

      public void isNull() {
        assertThat(_collision).isNull();
      }

    }

  }
}
