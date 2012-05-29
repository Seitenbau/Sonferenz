package de.bitnoise.sonferenz.service.v2.services.impl.calculation1;

import static org.fest.assertions.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import de.bitnoise.sonferenz.service.v2.services.impl.calculation1.Calculate.VortragPair;

public class CalculateTest
{
  Calculate sut = new Calculate();

  int _id;

  @Test
  public void testJustAEmptyCall()
  {
    assertThat(sut.getResult()).isEmpty();
  }

  @Test(expected = NullPointerException.class)
  public void testWithNullList()
  {
    sut.calculate(null, 12);
  }

  @Test
  public void testGetGesetzteVortragIds_EmptyList()
  {
    List<Vortrag> liste = new ArrayList<Vortrag>();
    List<String> result = sut.getGesetzteVortragIds(liste);
    assertThat(result).isEmpty();
  }

  @Test
  public void testGetGesetzteVortragIds_EinVortrag()
  {
    List<Vortrag> liste = new ArrayList<Vortrag>();
    liste.add(vortrag("Rainer", "JUnit To The Max", "Rainer"));
    List<String> result = sut.getGesetzteVortragIds(liste);
    assertThat(result).hasSize(1);
    String v0 = result.get(0);
    assertThat(v0).isEqualTo("id-0");
  }

  @Test
  public void testContainsSpeaker_FindMatch()
  {
    List<Vortrag> liste = new ArrayList<Vortrag>();
    liste.add(vortrag("Rainer", "JUnit To The Max", "Person1"));
    liste.add(vortrag("Rainer", "AOP to the Max", "Person1", "Person2"));

    assertThat(sut.containsSpeaker(liste, speakers("Rainer"))).isTrue();
  }

  @Test
  public void testContainsSpeaker_NoMatch()
  {
    List<Vortrag> liste = new ArrayList<Vortrag>();
    liste.add(vortrag("Rainer", "JUnit To The Max", "Person1"));
    liste.add(vortrag("Rainer", "AOP to the Max", "Person1", "Person2"));

    assertThat(sut.containsSpeaker(liste, speakers("RaineR"))).isFalse();
  }

  @Test
  public void testGetGesetzteVortrage_ZweiVortraegeDuplicateSpeakerDifferentAttendendCount()
  {
    List<Vortrag> liste = new ArrayList<Vortrag>();
    liste.add(vortrag("Rainer", "JUnit To The Max", "Person1"));
    liste.add(vortrag("Rainer", "AOP to the Max", "Person1", "Person2"));
    List<Vortrag> result = sut.getGesetzteVortrage(liste, 12);
    assertThat(result).hasSize(1);
    Vortrag v0 = result.get(0);
    // sort works -> id-1 has more attendends
    assertThat(v0.getId()).isEqualTo("id-1");
  }

  @Test
  public void testGetGesetzteVortrage_ZweiVortraegeDuplicateSpeakerSameAttendendCount()
  {
    List<Vortrag> liste = new ArrayList<Vortrag>();
    liste.add(vortrag("Rainer", "JUnit To The Max", "Person1", "Person2"));
    liste.add(vortrag("Rainer", "AOP to the Max", "Person1", "Person2"));

    // execute
    List<Vortrag> result = sut.getGesetzteVortrage(liste, 12);

    // verify
    assertThat(result).hasSize(1);
    Vortrag v0 = result.get(0);
    // sort works, both same attendend count. use first Talk
    assertThat(v0.getId()).isEqualTo("id-0");
  }

  @Test
  public void testGetGesetzteVortrage_MehrereVortraegeSomeWithDuplicateSpeakers()
  {
    List<Vortrag> liste = new ArrayList<Vortrag>();
    liste.add(vortrag("Otto", "JUnit To The Max", "p1", "p2"));
    liste.add(vortrag("Rainer", "JUnit To The Max", "p1", "p2"));
    liste.add(vortrag("Muster2", "JUnit To The Max", "p1", "p2"));
    liste.add(vortrag("Rainer", "AOP to the Max", "p1", "p2"));
    liste.add(vortrag("Muster1", "AOP to the Max", "p1", "p2"));
    liste.add(vortrag("Rainer", "AOP to the Max", "p1", "p2"));

    // execute
    List<Vortrag> result = sut.getGesetzteVortrage(liste, 12);

    // verify
    assertThat(result).hasSize(4);
    assertThat(result.get(0).getId()).isEqualTo("id-0");
    assertThat(result.get(1).getId()).isEqualTo("id-1");
    assertThat(result.get(2).getId()).isEqualTo("id-2");
    assertThat(result.get(3).getId()).isEqualTo("id-4");
  }

  @Test
  public void testGetGesetzteVortrage_MehrereVortraegeSomeWithDuplicateSpeakersAndLimit()
  {
    List<Vortrag> liste = new ArrayList<Vortrag>();
    liste.add(vortrag("Otto", "JUnit To The Max", "p1", "p2"));
    liste.add(vortrag("Rainer", "JUnit To The Max", "p1", "p2"));
    liste.add(vortrag("Muster2", "JUnit To The Max", "p1", "p2"));
    liste.add(vortrag("Rainer", "AOP to the Max", "p1", "p2"));
    liste.add(vortrag("Muster1", "AOP to the Max", "p1", "p2"));
    liste.add(vortrag("Rainer", "AOP to the Max", "p1", "p2"));

    // execute
    List<Vortrag> result = sut.getGesetzteVortrage(liste, 2);

    // verify
    assertThat(result).hasSize(2);
    assertThat(result.get(0).getId()).isEqualTo("id-0");
    assertThat(result.get(1).getId()).isEqualTo("id-1");
  }

  @Test
  public void testCalculate_1()
  {
    List<Vortrag> liste = new ArrayList<Vortrag>();
    liste.add(vortrag("Otto", "JUnit To The Max", "p1", "p2"));
    liste.add(vortrag("Rainer", "JUnit To The Max", "p1", "p2"));
    liste.add(vortrag("Muster2", "JUnit To The Max", "p1", "p2"));
    liste.add(vortrag("Rainer", "AOP to the Max", "p1", "p2"));
    liste.add(vortrag("Muster1", "AOP to the Max", "p1", "p2"));
    liste.add(vortrag("Rainer", "AOP to the Max", "p1", "p2"));

    // execute
    List<VortragPair> result = sut.calculate(liste, 12);

    // verify
    assertThat(result).hasSize(2);
    VortragPair p0 = result.get(0);
    checkThat(p0, "id-4", "id-2");
    VortragPair p1 = result.get(1);
    checkThat(p0, "id-1", "id-0");
  }

  private void checkThat(VortragPair v, String... ids)
  {
    List<String> liste = new ArrayList<String>();
    liste.add(v.getVortrag1().getId());
    liste.add(v.getVortrag2().getId());
  }

  @Test
  public void testCalculate_withReferent()
  {
    List<Vortrag> liste = new ArrayList<Vortrag>();
    liste.add(vortrag("p1", "v0", "p0"));
    liste.add(vortrag("p2", "v1", "p0"));
    liste.add(vortrag("p3", "v2", "p0"));
    liste.add(vortrag("p4", "v3", "p1"));

    // execute
    List<VortragPair> result = sut.calculate(liste, 12);

    // verify
    assertThat(result).hasSize(2);
    VortragPair p0 = result.get(0);
    checkThat(p0, "id-3", "id-2");
    VortragPair p1 = result.get(1);
    checkThat(p1, "id-1", "id-0");
  }

  @Test
  public void testCalculate_withReferentAndAttendendCollision()
  {
    List<Vortrag> liste = new ArrayList<Vortrag>();
    liste.add(vortrag("Otto", "v0", "x", "p2"));
    liste.add(vortrag("Rainer", "v1", "Otto", "p2"));
    liste.add(vortrag("Muster2", "v2", "Otto", "p2"));
    liste.add(vortrag("Rainer", "v3", "p1", "p2"));
    liste.add(vortrag("Muster1", "v4", "p1", "p2"));
    liste.add(vortrag("Rainer", "v5", "p1", "p2"));

    // execute
    List<VortragPair> result = sut.calculate(liste, 12);

    // verify
    assertThat(result).hasSize(2);
    VortragPair p0 = result.get(0);
    checkThat(p0, "id-2", "id-4");
    VortragPair p1 = result.get(1);
    checkThat(p1, "id-1", "id-0");
  }

  @Test
  public void testCalculate_withReferentCollision()
  {
    List<Vortrag> liste = new ArrayList<Vortrag>();
    liste.add(vortrag("Otto", "v0", "x", "p1"));
    liste.add(vortrag("Rainer", "v1", "Otto", "p2"));
    liste.add(vortrag("Muster2", "v2", "Otto", "p3"));
    liste.add(vortrag("Muster1", "v4", "p8", "p5"));

    // execute
    List<VortragPair> result = sut.calculate(liste, 12);

    // verify
    assertThat(result).hasSize(2);
    VortragPair p0 = result.get(0);
    checkThat(p0, "id-2", "id-4");
    VortragPair p1 = result.get(1);
    checkThat(p1, "id-1", "id-0");
  }

  @Test
  public void testCalculate_WithLimit()
  {
    List<Vortrag> liste = new ArrayList<Vortrag>();
    liste.add(vortrag("Otto", "JUnit To The Max", "p1", "p2"));
    liste.add(vortrag("Rainer", "JUnit To The Max", "p1", "p2"));
    liste.add(vortrag("Muster2", "JUnit To The Max", "p1", "p2"));
    liste.add(vortrag("Rainer", "AOP to the Max", "p1", "p2"));
    liste.add(vortrag("Muster1", "AOP to the Max", "p1", "p2"));
    liste.add(vortrag("Rainer", "AOP to the Max", "p1", "p2"));

    // execute
    List<VortragPair> result = sut.calculate(liste, 2);

    // verify
    assertThat(result).hasSize(1);
    VortragPair p0 = result.get(0);
    assertThat(p0.getVortrag1().getId()).isEqualTo("id-0");
    assertThat(p0.getVortrag2().getId()).isEqualTo("id-1");
  }

  Vortrag vortrag(String vortragender, String titel, String... teilnehmner)
  {
    Vortrag v = new Vortrag("id-" + _id++);
    v.setTitel(titel);
    v.getReferenten().add(vortragender);
    v.getTeilnehmer().addAll(Arrays.asList(teilnehmner));
    return v;
  }

  Set<String> speakers(String... speakers)
  {
    Set<String> speakerSet = new HashSet<String>();
    speakerSet.addAll(Arrays.asList(speakers));
    return speakerSet;
  }

  @Test
  public void testCalculate_2012()
  {
    List<Vortrag> liste = new ArrayList<Vortrag>();
    liste.add(vortrag("CF", "10 Luegen", "CF"));
    liste.add(vortrag("IA", "Agile is not", "IA"));
    liste.add(vortrag("SB", "Android Development"));
    liste.add(vortrag("RW", "AOP"));
    liste.add(vortrag("NH", "Selenium"));
    liste.add(vortrag("TF", "CodeGen"));
    liste.add(vortrag("AB", "Leiden"));
    liste.add(vortrag("JS", "firebug"));
    liste.add(vortrag("AH", "Javascript"));
    liste.add(vortrag("PW", "Responsive"));
    liste.add(vortrag("JH", "Design"));
    liste.add(vortrag("AB", "TDD"));

    // execute
    List<VortragPair> result = sut.calculate(liste, 12);
    // verify
    for (VortragPair vp : result)
    {
      System.out.println(vp.getVortrag1() + " : " + vp.getVortrag2());
    }
    assertThat(result).hasSize(6);
    // VortragPair p0 = result.get(0);
    // assertThat(p0.getVortrag1().getId()).isEqualTo("id-0");
    // assertThat(p0.getVortrag2().getId()).isEqualTo("id-1");
  }

}
