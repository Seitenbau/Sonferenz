package de.bitnoise.sonferenz.service.v2.services.impl.calculation2;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;

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
  Person p = new Person("p");

  Talk t1 = new Talk("t1")
      .addSpeaker(p1)
      .addVisitor(p4, p5);
  Talk t2 = new Talk("t2")
      .addSpeaker(p2, p3)
      .addVisitor(p6);
  Talk t3 = new Talk("t3")
      .addSpeaker(p3)
      .addVisitor(p1, p2, p3, p4, p5, p6);
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

    assertThat(result.collision(t2, t1).getColliders()).isEmpty();
    assertThat(result.collision(t2, t2).getColliders()).containsOnly(p2, p3, p6);
    assertThat(result.collision(t2, t3).getColliders()).containsOnly(p6, p2, p3);
    assertThat(result.collision(t2, t4).getColliders()).containsOnly(p6);

    assertThat(result.collision(t3, t1).getColliders()).containsOnly(p1, p4, p5);
    assertThat(result.collision(t3, t2).getColliders()).containsOnly(p2, p3, p6);
    assertThat(result.collision(t3, t3).getColliders()).containsOnly(p1, p2, p3, p4, p5, p6);
    assertThat(result.collision(t3, t4).getColliders()).containsOnly(p6);

    assertThat(result.collision(t4, t1).getColliders()).isEmpty();
    assertThat(result.collision(t4, t2).getColliders()).containsOnly(p6);
    assertThat(result.collision(t4, t3).getColliders()).containsOnly(p6);
    assertThat(result.collision(t4, t4).getColliders()).containsOnly(p6);
  }
}
