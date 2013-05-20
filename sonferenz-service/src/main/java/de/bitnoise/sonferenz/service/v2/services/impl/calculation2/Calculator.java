package de.bitnoise.sonferenz.service.v2.services.impl.calculation2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Calculator
{
  Map<Object, Talk> talks = new HashMap<Object, Talk>();
  Map<Object, Person> persons = new HashMap<Object, Person>();

  public Calculator(Talk... talkList)
  {
    for (Talk t : talkList)
    {
      talks.put(t.getId(), t);
      addPersons(t.getSpeakers());
      addPersons(t.getVisitors());
    }
  }

  void addPersons(List<Person> personList)
  {
    for (Person p : personList)
    {
      persons.put(p.getId(), p);
    }
  }

  public Collisions calculateCollisions()
  {
    Collisions result = new Collisions();
    Collection<Talk> allTalks = talks.values();

    List<TalkCollision> all = new ArrayList<TalkCollision>();
    // create Matrix
    for (Talk talkA : allTalks)
    {
      for (Talk talkB : allTalks)
      {
        TalkCollision col1 = calculateTalkCollision(talkA, talkB);
        result.add(col1);
        all.add(col1);
        TalkCollision col2 = calculateTalkCollision(talkA, talkB);
        result.add(col2);
        all.add(col2);
      }
    }
    List<TalkCollision> tmp = new ArrayList<TalkCollision>();
    for (TalkCollision item : all)
    {
      // if (item.getTalkA() != item.getTalkB())
      {
        tmp.add(item);
      }
    }

    return result;
  }

  public List<TalkCollision> calculate()
  {
    Collisions colls = calculateCollisions();
    List<TalkCollision> tmp = colls.getAll();
    Collections.sort(tmp, new Comparator<TalkCollision>() {
      @Override
      public int compare(TalkCollision o1, TalkCollision o2)
      {
        Integer s1 = o1.getColliders().size();
        Integer s2 = o2.getColliders().size();
        if (s1.equals(s2))
        {
          Integer w1 = o1.getWeight();
          Integer w2 = o2.getWeight();
          return w2.compareTo(w1);
        }
        else
        {
          return s1.compareTo(s2);
        }
      }
    });
    for (TalkCollision item : tmp)
    {
      print(item);
    }
    System.out.println("---------");
    List<Talk> talks = new ArrayList<Talk>();
    List<TalkCollision> roomless = new ArrayList<TalkCollision>();
    for (TalkCollision item : tmp)
    {
      if (talks.contains(item.getTalkA()) || talks.contains(item.getTalkB()))
      {
        continue;
      }
      if (item.isPossible())
      {
        talks.add(item.getTalkA());
        talks.add(item.getTalkB());
        roomless.add(item);
      }
    }
    List<TalkCollision> fin = new ArrayList<TalkCollision>();
    for (TalkCollision item : roomless)
    {
      int v1 = item.getTalkA().getVisitors().size();
      int v2 = item.getTalkB().getVisitors().size();
      Talk a = item.getTalkA();
      Talk b = item.getTalkB();
      boolean swap = false;
      if (v2 > v1)
      {
        swap = true;
      }
      if (swap)
      {
        System.out.println(a + " <-> " + b + " (" + v1 + ":" + v2 + ")");
        a = item.getTalkB();
        b = item.getTalkA();
      }
      boolean notPossible = !item.isPossible();
      List<Person> collidedPersons = item.getColliders();
      fin.add(new TalkCollision(a, b, notPossible, collidedPersons));
    }
    for (TalkCollision item : fin)
    {
      print(item);
    }
    return fin;
  }

  private void print(TalkCollision item)
  {
    System.out.print(item.hashCode() + "\t" + item.getTalkA() + "> " + item.getColliders().size() + " <"
        + item.getTalkB() + " " + (item.isPossible() ? "[p] " : "[-] ") + item.getWeight());
    System.out.print(" [");
    for (WeightedVisit x : item.getWeights())
    {
      System.out.print("(" + x.person + ":" + x.weight + ")");
    }
    System.out.print("]");
    System.out.print(" (" +item.getTalkA().getVisitors().size() + ":" + item.getTalkB().getVisitors().size() + ")");
    System.out.println();
  }

  TalkCollision calculateTalkCollision(Talk talkA, Talk talkB)
  {
    // find out if both talks have the same speakers -> not possible
    List<Person> sa = talkA.getSpeakers();
    List<Person> sb = talkB.getSpeakers();
    boolean notPossible = false;
    for (Person s : sa)
    {
      if (sb.contains(s))
      {
        notPossible = true;
      }
    }
    // calculate collision of visitors
    TalksVisits va = new TalksVisits(talkA);
    TalksVisits vb = new TalksVisits(talkB);
    List<Person> colliders1 = new ArrayList<Person>();

    TalkCollision coll1 = new TalkCollision(talkA, talkB, notPossible, colliders1);
    for (WeightedVisit v : va)
    {
      if (vb.hasAPerson(v))
      {
        coll1.addWeight(v);
        colliders1.add(v.getPerson());
      }
    }
    return coll1;
  }

  Map<Object, Talk> getTalks()
  {
    return talks;
  }

  Map<Object, Person> getPersons()
  {
    return persons;
  }

}
