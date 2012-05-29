package de.bitnoise.sonferenz.service.v2.services.impl.calculation1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

public class Calculate
{

  private int round;

  private List<VortragPair> _result;

  public List<VortragPair> getResult()
  {
    if (_result == null)
    {
      return new ArrayList<VortragPair>();
    }
    return _result;
  }

  private static final int COLUMN_LENGTH = 4;

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception
  {
    VortragPlaintextReader vortragReader = new VortragPlaintextReader();
    new Calculate().run(vortragReader.read());
  }

  public List<VortragPair> calculate(List<Vortrag> vortragList, int maxTalks)
  {
    if (maxTalks % 2 != 0)
    {
      throw new IllegalArgumentException("maxTalks have to be a multiple of 2");
    }
    List<Vortrag> relevantTalks = getGesetzteVortrage(vortragList, maxTalks);

    System.out.println(printKollisionen(relevantTalks));

    round = 0;
    List<VortragPair> bestPairings = getPairings(relevantTalks);
//    System.out.println("rounds = " + round);
//    System.out.println(countCollisions(bestPairings) + " = " + bestPairings);
//    System.out.println(printPairings(bestPairings));
    _result = bestPairings;
    return _result;
  }

  public void run(List<Vortrag> vortragList) throws IOException, Exception
  {
    {
      Set<String> teilnehmer = new TreeSet<String>();
      Set<String> referenten = new TreeSet<String>();
      for (Vortrag vortrag : vortragList)
      {
        teilnehmer.addAll(vortrag.getTeilnehmer());
        referenten.addAll(vortrag.getReferenten());
      }

      System.out.println("Teilnehmer (" + teilnehmer.size() + "): " + teilnehmer);
      System.out.println("Referenten (" + referenten.size() + "): " + referenten);
      Set<String> personen = new TreeSet<String>();
      personen.addAll(teilnehmer);
      personen.addAll(referenten);
      System.out.println("Teilnehmer+Referenten (" + personen.size() + "): "
          + personen);
    }

    System.out.println(printTeilnehmerSize(vortragList));
    System.out.println(printTeilnehmerUndRerefernten(vortragList));

    Collection<String> gesetzteVortragIds = getGesetzteVortragIds(vortragList);
    List<Vortrag> gesetzteVortraege = new ArrayList<Vortrag>();
    for (Vortrag vortrag : vortragList)
    {
      if (gesetzteVortragIds.contains(vortrag.getId()))
      {
        gesetzteVortraege.add(vortrag);
      }
    }
    if (gesetzteVortragIds.size() != gesetzteVortraege.size())
    {
      throw new Exception("Anzahl Ids Vortraege " + gesetzteVortragIds.size()
          + " ist nicht gleich Anzahl Vortraege " + gesetzteVortraege.size()
          + "\n, Vortraege" + gesetzteVortraege);
    }
    System.out.println(printKollisionen(gesetzteVortraege));

    round = 0;
    List<VortragPair> bestPairings = getPairings(gesetzteVortraege);
    System.out.println("rounds = " + round);
    System.out.println(countCollisions(bestPairings) + " = " + bestPairings);
    System.out.println(printPairings(bestPairings));
    _result = bestPairings;
  }

//  /**
//   * Falls die gesetzten Vortr�ge von Hand bestimmt werden sollen diese Methode
//   * verwenden
//   */
//  private static Set<String> getGesetzteVortagIdsFromConstant(
//      List<Vortrag> vortragList)
//  {
//    Set<String> gesetzteVortragIds = new HashSet<String>();
//    gesetzteVortragIds.add("1");
//    gesetzteVortragIds.add("2");
//    gesetzteVortragIds.add("4");
//    gesetzteVortragIds.add("6");
//    gesetzteVortragIds.add("9");
//    gesetzteVortragIds.add("10");
//    gesetzteVortragIds.add("11");
//    gesetzteVortragIds.add("13");
//    gesetzteVortragIds.add("14");
//    gesetzteVortragIds.add("15");
//    gesetzteVortragIds.add("16");
//    gesetzteVortragIds.add("19");
//    return gesetzteVortragIds;
//  }

  /**
   * Create a List of most requested Talks with no duplicate Speakers.
   * 
   * @param talksList
   *          List of available Talks. Not {@code null}
   * 
   * @param maxVortraege
   *          Maximum Talks allowed for Conference.
   * 
   * @return List of Talks, with the most attendends. No Talks with the same
   *         Speaker. List has no more thant maxTalks entries.
   */
  public List<Vortrag> getGesetzteVortrage(List<Vortrag> talksList, int maxTalks)
  {
    // work with a copy
    List<Vortrag> talksByAttendends = new ArrayList<Vortrag>(talksList);

    // Sort by attendends count
    Collections.sort(talksByAttendends,
        new Vortrag.VortragTeilnehmerComparator());

    // Remove all duplicate Speakers, and limit list size
    List<Vortrag> result = new ArrayList<Vortrag>();
    Iterator<Vortrag> iter = talksByAttendends.iterator();
    for (int i = 0; result.size() < maxTalks && iter.hasNext(); i++)
    {
      Vortrag talk = iter.next();
     if (!containsSpeaker(result, talk.getReferenten()))
      {
        result.add(talk);
      }
    }
    return result;
  }

  /**
   * Check if given list contains one of the given speakers.
   * 
   * @param talkList
   *          List of Talks the where the speakers should be searched in.
   * @param speakers
   *          List Of Speaker Names to search for
   * @return true if one of the speakers is in the given list
   */
  protected boolean containsSpeaker(List<Vortrag> talkList, Set<String> speakers)
  {
    if (talkList == null)
    {
      return false;
    }
    for (Vortrag v : talkList)
    {
      for (String speaker : speakers)
      {
        if (v.getReferenten().contains(speaker))
        {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * die Vortr�ge mit den meisten Stimmen werden gesetzt, kein Referent doppelt.
   */
  protected List<String> getGesetzteVortragIds(List<Vortrag> vortragList)
  {
    int numberOfVortraege = vortragList.size() - 1;
    if (numberOfVortraege > 12)
    {
      numberOfVortraege = 12;
    }
    List<Vortrag> gesetzteVortraege = new ArrayList<Vortrag>(vortragList);
    Collections.sort(gesetzteVortraege,
        new Vortrag.VortragTeilnehmerComparator());
    List<String> result = new ArrayList<String>();
    System.out.println("\nGesetzte Vortr�ge:");
    int i = 0;
    while (result.size() <= numberOfVortraege)
    {
      Vortrag vortrag = gesetzteVortraege.get(i);
      boolean doppelterReferent = false;
      OuterLoop: for (int j = 0; j < i; ++j)
      {
        for (String referent : vortrag.getReferenten())
        {
          if (gesetzteVortraege.get(j).getReferenten().contains(referent))
          {
            System.out.println("Doppelter Referent " + referent
                + " in Vortraegen " + i + " und " + j);
            doppelterReferent = true;
            break OuterLoop;
          }
        }
      }
      if (!doppelterReferent)
      {
        result.add(vortrag.getId());
      }
      else
      {
        System.out.println("nicht gesetzt wegen doppeltem Referent: "
            + vortrag.toString());
      }
      System.out.println(vortrag.toString() + " : "
          + vortrag.getTeilnehmer().size());
      ++i;
    }

    System.out.println("\nNicht Gesetzte Vortr�ge:");
    for (; i < gesetzteVortraege.size(); ++i)
    {
      Vortrag vortrag = gesetzteVortraege.get(i);
      System.out.println(vortrag.toString() + " : "
          + vortrag.getTeilnehmer().size());
    }
    return result;
  }

  private static String printTeilnehmerSize(List<Vortrag> vortragList)
  {
    StringBuilder result = new StringBuilder();
    result.append("Id|TeilnehmerSize\n");
    for (Vortrag vortrag : vortragList)
    {
      result.append(StringUtils.rightPad(vortrag.getId(), COLUMN_LENGTH));
      result.append("|");
      String teilnehmerSize = Integer.toString(vortrag.getTeilnehmer().size());
      result.append(StringUtils.rightPad(teilnehmerSize, COLUMN_LENGTH));
      result.append("\n");
    }
    result.append("\n\n");
    return result.toString();
  }

  private static String printTeilnehmerUndRerefernten(List<Vortrag> vortragList)
  {
    StringBuilder result = new StringBuilder();
    result.append("Id|Referenten|Teilnehmer\n");
    for (Vortrag vortrag : vortragList)
    {
      result.append(StringUtils.rightPad(vortrag.getId(), COLUMN_LENGTH));
      result.append("|");
      String referenten = vortrag.getReferenten().toString();
      result.append(StringUtils.rightPad(referenten, 25));
      String teilnehmer = vortrag.getTeilnehmer().toString();
      result.append(StringUtils.rightPad(teilnehmer, COLUMN_LENGTH));
      result.append("\n");
    }
    result.append("\n\n");
    return result.toString();
  }

  private static String printKollisionen(List<Vortrag> vortragList)
  {
    StringBuilder result = new StringBuilder();

    // headline
    result.append("Kollisionen\n");
    result.append(StringUtils.rightPad("", COLUMN_LENGTH));
    result.append("|");
    for (Vortrag vortrag : vortragList)
    {
      result.append(StringUtils.rightPad(vortrag.getId(), COLUMN_LENGTH));
    }
    result.append("\n");
    result.append(StringUtils.rightPad("", 1 + (vortragList.size() + 1)
        * COLUMN_LENGTH, '-'));
    result.append("\n");
    for (Vortrag vortrag : vortragList)
    {
      result.append(StringUtils.rightPad(vortrag.getId(), COLUMN_LENGTH));
      result.append("|");
      for (Vortrag otherVortrag : vortragList)
      {
        if (otherVortrag == vortrag)
        {
          result.append(StringUtils.rightPad("", COLUMN_LENGTH));
          continue;
        }
        int kollisionen = vortrag.kollisionen(otherVortrag);
        String kollisionenString = Integer.toString(kollisionen);
        result.append(StringUtils.rightPad(kollisionenString, COLUMN_LENGTH));
      }
      result.append("\n");
    }
    result.append("\n\n");
    return result.toString();
  }

  protected List<VortragPair> getPairings(Collection<Vortrag> vortragList)
  {
    List<VortragPair> result = new ArrayList<VortragPair>();
    if (vortragList.size() == 2)
    {
      result.add(getPairingFromCollection(vortragList));
      return result;
    }

    List<VortragPair> bestPairings = null;
    Integer minCollisions = Integer.MAX_VALUE;
    for (Vortrag vortrag1 : vortragList)
    {
      for (Vortrag vortrag2 : vortragList)
      {
        if (vortrag2 == vortrag1)
        {
          // nur eine Seite des Dreiecks durchlaufen
          break;
        }
        Set<Vortrag> remaining = new HashSet<Vortrag>(vortragList);
        remaining.remove(vortrag1);
        remaining.remove(vortrag2);
        List<VortragPair> pairings = getPairings(remaining);
        int count = countCollisions(pairings);
        count += vortrag1.kollisionen(vortrag2);
        if (count < minCollisions)
        {
          if(pairings==null) {
            return new ArrayList<Calculate.VortragPair>();
          }
          minCollisions = count;
          bestPairings = pairings;
          bestPairings.add(new VortragPair(vortrag1, vortrag2));
        }
      }
    }
    return bestPairings;
  }

  private static int countCollisions(List<VortragPair> pairList)
  {
    int result = 0;
    if (pairList == null)
    {
      return 0;
    }
    for (VortragPair pair : pairList)
    {
      result += pair.getKollisionen();
    }
    return result;
  }

  private static String printPairings(List<VortragPair> pairList)
  {
    int i = 1;
    StringBuilder result = new StringBuilder();
    for (VortragPair vortragPair : pairList)
    {
      result.append("Paarung " + i + ":\n");
      result.append(vortragPair.getVortrag1().getId())
          .append(vortragPair.getVortrag1().getReferenten()).append(":")
          .append(vortragPair.getVortrag1().getTitel()).append("\n");
      result.append(vortragPair.getVortrag2().getId())
          .append(vortragPair.getVortrag2().getReferenten()).append(":")
          .append(vortragPair.getVortrag2().getTitel()).append("\n");
      ++i;
    }
    return result.toString();
  }

  private static VortragPair getPairingFromCollection(
      Collection<Vortrag> vortragCollection)
  {
    if (vortragCollection.size() != 2)
    {
      throw new IllegalArgumentException();
    }
    Iterator<Vortrag> vortragIt = vortragCollection.iterator();
    VortragPair pairing = new VortragPair(vortragIt.next(), vortragIt.next());
    return pairing;
  }

  public static class VortragPair
  {
    private int kollisionen;

    private Vortrag vortrag1;

    private Vortrag vortrag2;

    public VortragPair(Vortrag vortrag1, Vortrag vortrag2)
    {
      this.vortrag1 = vortrag1;
      this.vortrag2 = vortrag2;
      this.kollisionen = vortrag1.kollisionen(vortrag2);
    }

    public int getKollisionen()
    {
      return kollisionen;
    }

    public Vortrag getVortrag1()
    {
      return vortrag1;
    }

    public Vortrag getVortrag2()
    {
      return vortrag2;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
      StringBuilder builder = new StringBuilder();
      builder.append("[");
      builder.append(vortrag1.getId());
      builder.append(",");
      builder.append(vortrag2.getId());
      builder.append("]");
      return builder.toString();
    }
  }

  private static class Pairing
  {
    private List<Vortrag> remaining;

    private VortragPair pair;

    public Pairing(List<Vortrag> remaining, VortragPair pair)
    {
      this.remaining = remaining;
      this.pair = pair;
    }

    /**
     * @return the remaining
     */
    public List<Vortrag> getRemaining()
    {
      return remaining;
    }

    /**
     * @return the pair
     */
    public VortragPair getPair()
    {
      return pair;
    }

  }

}
