package de.bitnoise.sonferenz.service.v2.services.impl.calculation2;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import de.bitnoise.sonferenz.service.v2.UnitTestBase;
import de.bitnoise.testing.mockito.MockitoRule;
import static org.fest.assertions.Assertions.*;

public class TalkTest extends UnitTestBase
{
  @Mock
  Person p1, p2, p3, p4;

  @Test
  public void create()
  {
    // prepare
    String id = "t1";
    // execute
    Talk sut = new Talk(id);
    // verify
    assertThat(sut.getId()).isSameAs(id);
    assertThat(sut.getSpeakers()).isEmpty();
    assertThat(sut.getVisitors()).isEmpty();
  }

  @Test
  public void addSpeaker_addOneSpeaker()
  {
    // prepare
    String id = "t1";
    // execute
    Talk sut = new Talk(id);
    sut.addSpeaker(p1);

    // verify
    assertThat(sut.getId()).isSameAs(id);
    assertThat(sut.getSpeakers()).containsExactly(p1);
    assertThat(sut.getVisitors()).isEmpty();
  }

  @Test
  public void addSpeaker_addThreeSpeakers_RemoveDuplicates()
  {
    // prepare
    String id = "t1";
    // execute
    Talk sut = new Talk(id);
    sut.addSpeaker(p1, p2);
    sut.addSpeaker(p2, p3);

    // verify
    assertThat(sut.getId()).isSameAs(id);
    assertThat(sut.getSpeakers()).containsExactly(p1, p2, p3);
    assertThat(sut.getVisitors()).isEmpty();
  }

  @Test
  public void addVisitor_addOne()
  {
    // prepare
    String id = "t1";
    // execute
    Talk sut = new Talk(id);
    sut.addVisitor(p1);

    // verify
    assertThat(sut.getId()).isSameAs(id);
    assertThat(sut.getSpeakers()).isEmpty();
    assertThat(sut.getVisitors()).containsExactly(p1);
  }

  @Test
  public void addVisitor_addThreeRemoveSpeakers()
  {
    // prepare
    String id = "t1";
    // execute
    Talk sut = new Talk(id);
    sut.addSpeaker(p2, p3);
    sut.addVisitor(p1, p2, p4);
    sut.addVisitor(p2, p3);

    // verify
    assertThat(sut.getId()).isSameAs(id);
    assertThat(sut.getSpeakers()).containsExactly(p2, p3);
    assertThat(sut.getVisitors()).containsExactly(p1, p4);
  }

  @Test
  public void addVisitor_failsOnSamePersonAgain()
  {
    // prepare
    String id = "t1";

    // execute part 1
    Talk sut = new Talk(id);
    sut.addSpeaker(p2, p3);
    sut.addVisitor(p1, p2, p4);

    // verify
    expectedException(IllegalStateException.class, "This talk allready contains that person. talk=t1 person=p4");

    // adding p4 will fail
    sut.addVisitor(p2, p3, p4);

  }

}
