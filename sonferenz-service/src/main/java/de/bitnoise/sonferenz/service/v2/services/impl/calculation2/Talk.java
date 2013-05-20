package de.bitnoise.sonferenz.service.v2.services.impl.calculation2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Talk
{
  Object id;
  List<Person> speakers = new ArrayList<Person>();

  List<Person> visitorPersons = new ArrayList<Person>();
  List<Visit> visitors = new ArrayList<Visit>();

  public Talk(String name)
  {
    id = name;
  }

  @Override
  public String toString()
  {
    return id.toString();
  }

  public Talk addSpeaker(Person... speakerList)
  {
    for (Person s : speakerList)
    {
      if (!speakers.contains(s))
      {
        speakers.add(s);
      }
    }
    return this;
  }

  public Talk addVisitor(Visit... visit)
  {
    for (Visit v : visit)
    {
      Person person = v.getPerson();
      if (!speakers.contains(person))
      {
        if (visitorPersons.contains(person))
        {
          throw new IllegalStateException(
              "This talk allready contains that person." +
                  " talk=" + this +
                  " person=" + v.getPerson());
        }
        visitorPersons.add(person);
        visitors.add(v);
      }
    }
    return this;
  }

  public Object getId()
  {
    return id;
  }

  public List<Person> getSpeakers()
  {
    return speakers;
  }

  public List<Visit> getVisits()
  {
    return visitors;
  }
  
  public List<Person> getVisitors()
  {
    return visitorPersons;
  }
}
