package de.bitnoise.sonferenz.service.v2.services.impl.calculation2;

import java.util.ArrayList;
import java.util.Collection;
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

    // create Matrix
    for (Talk talkA : allTalks)
    {
      for (Talk talkB : allTalks)
      {
        TalkCollision col = calculateTalkCollision(talkA, talkB);
        result.add(col);
      }
    }
    return result;
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
    List<Person> va = new ArrayList<Person>();
    va.addAll(talkA.getVisitors());
    va.addAll(talkA.getSpeakers());
    List<Person> vb = new ArrayList<Person>();
    vb.addAll(talkB.getVisitors());
    vb.addAll(talkB.getSpeakers());
    List<Person> colliders = new ArrayList<Person>();
    for (Person v : va)
    {
      if (vb.contains(v))
      {
        colliders.add(v);
      }
    }
    return new TalkCollision(talkA, talkB, notPossible, colliders);
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
