package de.bitnoise.sonferenz.service.v2.services.impl.calculation;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CalcTalk;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CalcUser;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CollisionResult;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.Collisions;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.ConstraintFunction;

public class CalculatorLogicTest {

  CalculationConfigurationImpl config = new CalculationConfigurationImpl();
  CalculatorLogic sut = new CalculatorLogic(config);

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

  ConstraintFunction visit = new ConstraintFunction() {
    @Override
    public Integer calculate(CalculationTalkImpl talkB, CalcUser user) {
      if (talkB.containsUser(user)) {
        return 1;
      }
      return null;
    }
  };
  ConstraintFunction speaker = new ConstraintFunction() {
    @Override
    public Integer calculate(CalculationTalkImpl talkB, CalcUser user) {
      if (talkB.containsUser(user)) {
        return -1;
      }
      return null;
    }
  };

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
    talk1.addConstraint(speaker, user1);
    talk2.addConstraint(speaker, user2);
    talk3.addConstraint(speaker, user3);
    talk4.addConstraint(speaker, user4);

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
    talk1.addConstraint(speaker, user1);
    talk1.addConstraint(speaker, user2);
    talk2.addConstraint(speaker, user2);
    talk3.addConstraint(speaker, user3);
    talk4.addConstraint(speaker, user4);

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
    talk1.addConstraint(speaker, user1);
    talk1.addConstraint(speaker, user3);
    talk2.addConstraint(speaker, user2);
    talk3.addConstraint(speaker, user3);
    talk4.addConstraint(speaker, user4);
    talk4.addConstraint(speaker, user1);

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

    talk1.addConstraint(speaker, user1);
    talk1.addConstraint(speaker, user3);
    talk2.addConstraint(speaker, user2);
    talk3.addConstraint(speaker, user3);
    talk4.addConstraint(speaker, user4);

    talk4.addConstraint(visit, user1);

    // execute
    CollisionResult result = sut.calculateCollisions();

    // verify
    check(result).talk(talk1, talk1).isImpossible();
    check(result).talk(talk1, talk2).isPossible(0);
    check(result).talk(talk1, talk3).isImpossible();
    check(result).talk(talk1, talk4).isPossible(1);
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

    talk1.addConstraint(speaker, user1);
    talk1.addConstraint(speaker, user3);
    talk2.addConstraint(speaker, user2);
    talk3.addConstraint(speaker, user3);
    talk4.addConstraint(speaker, user4);

    talk4.addConstraint(visit, user1);
    talk4.addConstraint(visit, user2);
    talk4.addConstraint(visit, user3);
    talk4.addConstraint(visit, user4);

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

    talk1.addConstraint(speaker, user1);
    talk1.addConstraint(speaker, user3);
    talk2.addConstraint(speaker, user2);
    talk3.addConstraint(speaker, user3);
    talk4.addConstraint(speaker, user4);

    talk4.addConstraint(visit, user1);
    talk4.addConstraint(visit, user2);
    talk4.addConstraint(visit, user3);
    talk4.addConstraint(visit, user4);

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
    
    talk3.addConstraint(visit, user2);
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
