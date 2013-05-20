package de.bitnoise.sonferenz.service.v2.services.impl.calculation2;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;

import java.util.List;

import org.junit.Test;

import de.bitnoise.sonferenz.service.v2.UnitTestBase;

public class CalculatorTest extends UnitTestBase
{
  Person p1 = new Person("p1"); // speaker talk1
  Person p2 = new Person("p2"); // speaker talk2
  Person p3 = new Person("p3"); // speaker talk2
  Person p4 = new Person("p4");
  Person p5 = new Person("p5");
  Person p6 = new Person("p6");
  Person p7 = new Person("p7");
  Person p = new Person("p");

  Talk t1 = new Talk("t1")
      .addSpeaker(p1)
      .addVisitor(v(p4, 2), v(p5, 1));
  Talk t2 = new Talk("t2")
      .addSpeaker(p2, p3)
      .addVisitor(v(p6, 1));
  Talk t3 = new Talk("t3")
      .addSpeaker(p3)
      .addVisitor(v(p1, 6), v(p2, 5), v(p3, 4), v(p4, 3), v(p5, 2), v(p6, 1));
  Talk t4 = new Talk("t4")
      .addSpeaker(p6);

  @Test
  public void create()
  {
    // prepare
    // execute
    Calculator sut = new Calculator();

    // verify
    assertThat(sut.getPersons()).isEmpty();
    assertThat(sut.getTalks()).isEmpty();
  }

  Visit v(Person person, int weight)
  {
    return new Visit(person, weight);
  }

  @Test
  public void createWithTalks()
  {
    // prepare
    // execute
    Calculator sut = new Calculator(t1, t2, t3, t4);

    // verify
    assertThat(sut.getPersons())
        .includes(entry("p1", p1), entry("p2", p2), entry("p3", p3), entry("p4", p4), entry("p5", p5), entry("p6", p6))
        .excludes(entry("p", p))
        .hasSize(6);
    assertThat(sut.getTalks())
        .includes(entry("t1", t1), entry("t2", t2), entry("t3", t3), entry("t4", t4))
        .hasSize(4);
  }

  @Test
  public void calculateTalkCollision_t1t1()
  {
    // prepare
    Calculator sut = new Calculator();

    // execute
    TalkCollision result = sut.calculateTalkCollision(t1, t1);

    // verify
    assertThat(result.getTalkA()).isSameAs(t1);
    assertThat(result.getTalkB()).isSameAs(t1);
    // visitors + speakers
    assertThat(result.getColliders()).containsOnly(p1, p4, p5);
    assertThat(result.isPossible()).isFalse();
  }

  @Test
  public void calculateTalkCollision_t1t2()
  {
    // prepare
    Calculator sut = new Calculator();

    // execute
    TalkCollision result = sut.calculateTalkCollision(t1, t2);

    // verify
    assertThat(result.getTalkA()).isSameAs(t1);
    assertThat(result.getTalkB()).isSameAs(t2);
    // visitors + speakers
    assertThat(result.getColliders()).isEmpty();
    assertThat(result.isPossible()).isTrue();
  }

  @Test
  public void calculateTalkCollision_t2t3_IsNotPossible_BothTalksHaveSameSpeaker()
  {
    // prepare
    Calculator sut = new Calculator();

    // execute
    TalkCollision result = sut.calculateTalkCollision(t2, t3);

    // verify
    assertThat(result.getTalkA()).isSameAs(t2);
    assertThat(result.getTalkB()).isSameAs(t3);
    // visitors + speakers
    assertThat(result.getColliders()).containsOnly(p6, p2, p3);
    assertThat(result.isPossible()).isFalse();
  }

  @Test
  public void calculateTalkCollision_t3t4_SpeakerIsAlsoVisitor()
  {
    // prepare
    Calculator sut = new Calculator();

    // execute
    TalkCollision result = sut.calculateTalkCollision(t3, t4);

    // verify
    assertThat(result.getTalkA()).isSameAs(t3);
    assertThat(result.getTalkB()).isSameAs(t4);
    // visitors + speakers
    assertThat(result.getColliders()).containsOnly(p6);
    assertThat(result.isPossible()).isTrue();
  }

  @Test
  public void calculateTalkCollision_t1t3()
  {
    // prepare
    Calculator sut = new Calculator();

    // execute
    TalkCollision result = sut.calculateTalkCollision(t1, t3);

    // verify
    assertThat(result.getTalkA()).isSameAs(t1);
    assertThat(result.getTalkB()).isSameAs(t3);
    // visitors + speakers
    assertThat(result.getColliders()).containsOnly(p4, p5, p1);
    assertThat(result.isPossible()).isTrue();
  }

  @Test
  public void calculateTalkCollision_t1t4()
  {
    // prepare
    Calculator sut = new Calculator();

    // execute
    TalkCollision result = sut.calculateTalkCollision(t1, t4);

    // verify
    assertThat(result.getTalkA()).isSameAs(t1);
    assertThat(result.getTalkB()).isSameAs(t4);
    // visitors + speakers
    assertThat(result.getColliders()).isEmpty();
    assertThat(result.isPossible()).isTrue();
  }

  @Test
  public void calculateTalkCollision_t2t4()
  {
    // prepare
    Calculator sut = new Calculator();

    // execute
    TalkCollision result = sut.calculateTalkCollision(t2, t4);

    // verify
    assertThat(result.getTalkA()).isSameAs(t2);
    assertThat(result.getTalkB()).isSameAs(t4);
    // visitors + speakers
    assertThat(result.getColliders()).containsOnly(p6);
    assertThat(result.isPossible()).isTrue();
  }

  @Test
  public void calculateCollisions()
  {
    // prepare
    Calculator sut = new Calculator(t1, t2, t3, t4);
    // execute
    Collisions result = sut.calculateCollisions();
    // verify
    assertThat(result.collision(t1, t1).getColliders()).containsOnly(p1, p4, p5);
    assertThat(result.collision(t1, t2).getColliders()).isEmpty();
    assertThat(result.collision(t1, t3).getColliders()).containsOnly(p4, p5, p1);
    assertThat(result.collision(t1, t4).getColliders()).isEmpty();
    assertThat(result.collision(t1, t1).getWeight()).isEqualTo(33);
    assertThat(result.collision(t1, t2).getWeight()).isEqualTo(0);
    assertThat(result.collision(t1, t3).getWeight()).isEqualTo(33);
    assertThat(result.collision(t1, t4).getWeight()).isEqualTo(0);

    assertThat(result.collision(t2, t1).getColliders()).isEmpty();
    assertThat(result.collision(t2, t2).getColliders()).containsOnly(p2, p3, p6);
    assertThat(result.collision(t2, t3).getColliders()).containsOnly(p6, p2, p3);
    assertThat(result.collision(t2, t4).getColliders()).containsOnly(p6);
    assertThat(result.collision(t2, t1).getWeight()).isEqualTo(0);
    assertThat(result.collision(t2, t2).getWeight()).isEqualTo(31);
    assertThat(result.collision(t2, t3).getWeight()).isEqualTo(31);
    assertThat(result.collision(t2, t4).getWeight()).isEqualTo(10);

    assertThat(result.collision(t3, t1).getColliders()).containsOnly(p1, p4, p5);
    assertThat(result.collision(t3, t2).getColliders()).containsOnly(p2, p3, p6);
    assertThat(result.collision(t3, t3).getColliders()).containsOnly(p1, p2, p3, p4, p5, p6);
    assertThat(result.collision(t3, t4).getColliders()).containsOnly(p6);
    assertThat(result.collision(t3, t1).getWeight()).isEqualTo(33);
    assertThat(result.collision(t3, t2).getWeight()).isEqualTo(31);
    assertThat(result.collision(t3, t3).getWeight()).isEqualTo(77); // speaker
                                                                    // not
    assertThat(result.collision(t3, t4).getWeight()).isEqualTo(10);

    assertThat(result.collision(t4, t1).getColliders()).isEmpty();
    assertThat(result.collision(t4, t2).getColliders()).containsOnly(p6);
    assertThat(result.collision(t4, t3).getColliders()).containsOnly(p6);
    assertThat(result.collision(t4, t4).getColliders()).containsOnly(p6);
    assertThat(result.collision(t4, t1).getWeight()).isEqualTo(0);
    assertThat(result.collision(t4, t2).getWeight()).isEqualTo(10);
    assertThat(result.collision(t4, t3).getWeight()).isEqualTo(10);
    assertThat(result.collision(t4, t4).getWeight()).isEqualTo(10);
  }

  @Test
  public void calculateCollisions_weighted()
  {
    Talk t1 = new Talk("t1").addSpeaker(p1, p2);
    Talk t2 = new Talk("t2").addSpeaker(p2, p3);
    Talk t3 = new Talk("t3").addSpeaker(p3, p4);
    Talk t4 = new Talk("t4").addSpeaker(p4, p1);

    // prepare
    Calculator sut = new Calculator(t1, t2, t3, t4);

    // execute
    sut.calculateCollisions();
    List<TalkCollision> result = sut.calculate();

    // verify
    TalkCollision slot1 = result.get(0);
    assertThat(slot1.getTalkA()).isEqualTo(t3);
    assertThat(slot1.getTalkB()).isEqualTo(t1);
    TalkCollision slot2 = result.get(1);
    assertThat(slot2.getTalkA()).isEqualTo(t2);
    assertThat(slot2.getTalkB()).isEqualTo(t4);
  }

  @Test
  public void calculateCollisions_weightedAllSame()
  {
    Talk t1 = new Talk("t1").addSpeaker(p1, p2)
        .addVisitor(v(p1, 1), v(p2, 1), v(p3, 1), v(p4, 1), v(p5, 1));
    Talk t2 = new Talk("t2").addSpeaker(p2, p3)
        .addVisitor(v(p1, 1), v(p2, 1), v(p3, 1), v(p4, 1), v(p5, 1));
    Talk t3 = new Talk("t3").addSpeaker(p3, p4)
        .addVisitor(v(p1, 1), v(p2, 1), v(p3, 1), v(p4, 1), v(p5, 1));
    Talk t4 = new Talk("t4").addSpeaker(p4, p1)
        .addVisitor(v(p1, 1), v(p2, 1), v(p3, 1), v(p4, 1), v(p5, 1));

    // prepare
    Calculator sut = new Calculator(t1, t2, t3, t4);

    // execute
    sut.calculateCollisions();
    List<TalkCollision> result = sut.calculate();

    // verify
    TalkCollision slot1 = result.get(0);
    assertThat(slot1.getTalkA()).isEqualTo(t3);
    assertThat(slot1.getTalkB()).isEqualTo(t1);
    TalkCollision slot2 = result.get(1);
    assertThat(slot2.getTalkA()).isEqualTo(t2);
    assertThat(slot2.getTalkB()).isEqualTo(t4);
  }

  @Test
  public void calculateCollisions_MoreForT1()
  {
    Talk t1 = new Talk("t1").addSpeaker(p1, p2)
        .addVisitor(v(p1, 2), v(p2, 1), v(p3, 1), v(p4, 1), v(p5, 1), v(p6, 1));
    Talk t2 = new Talk("t2").addSpeaker(p2, p3)
        .addVisitor(v(p1, 2), v(p2, 1), v(p3, 1), v(p4, 1), v(p5, 1));
    Talk t3 = new Talk("t3").addSpeaker(p3, p4)
        .addVisitor(v(p1, 2), v(p2, 1), v(p3, 1), v(p4, 1), v(p5, 1));
    Talk t4 = new Talk("t4").addSpeaker(p4, p1)
        .addVisitor(v(p1, 2), v(p2, 1), v(p3, 1), v(p4, 1), v(p5, 1));

    // prepare
    Calculator sut = new Calculator(t1, t2, t3, t4);

    // execute
    sut.calculateCollisions();
    List<TalkCollision> result = sut.calculate();

    // verify
    TalkCollision slot1 = result.get(0);
    assertThat(slot1.getTalkA()).isEqualTo(t1);
    assertThat(slot1.getTalkB()).isEqualTo(t3);
    TalkCollision slot2 = result.get(1);
    assertThat(slot2.getTalkA()).isEqualTo(t2);
    assertThat(slot2.getTalkB()).isEqualTo(t4);
  }

  @Test
  public void calculateCollisions_MoreForT1T4()
  {
    Talk t1 = new Talk("t1").addSpeaker(p1, p2)
        .addVisitor(v(p1, 2), v(p2, 1), v(p3, 1), v(p4, 1), v(p5, 1), v(p6, 1), v(p7, 1));
    Talk t2 = new Talk("t2").addSpeaker(p2, p3)
        .addVisitor(v(p1, 2), v(p2, 1), v(p3, 1), v(p4, 1), v(p5, 1));
    Talk t3 = new Talk("t3").addSpeaker(p3, p4)
        .addVisitor(v(p1, 2), v(p2, 1), v(p3, 1), v(p4, 1), v(p5, 1));
    Talk t4 = new Talk("t4").addSpeaker(p4, p1)
        .addVisitor(v(p1, 2), v(p2, 1), v(p3, 1), v(p4, 1), v(p5, 1), v(p6, 1));

    // prepare
    Calculator sut = new Calculator(t1, t2, t3, t4);

    // execute
    sut.calculateCollisions();
    List<TalkCollision> result = sut.calculate();

    // verify
    TalkCollision slot1 = result.get(0);
    assertThat(slot1.getTalkA()).isEqualTo(t1);
    assertThat(slot1.getTalkB()).isEqualTo(t3);
    TalkCollision slot2 = result.get(1);
    assertThat(slot2.getTalkA()).isEqualTo(t4);
    assertThat(slot2.getTalkB()).isEqualTo(t2);
  }
}
