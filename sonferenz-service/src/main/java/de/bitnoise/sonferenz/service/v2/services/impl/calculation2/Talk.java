package de.bitnoise.sonferenz.service.v2.services.impl.calculation2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Talk
{
  Object id;
  List<Person> speakers = new ArrayList<Person>();

  List<Person> visitors = new ArrayList<Person>();

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

  public Talk addVisitor(Person... visit)
  {
    for (Person v : visit)
    {
      if (!speakers.contains(v))
      {
        if (visitors.contains(v))
        {
          throw new IllegalStateException("This talk allready contains that person. talk=" + this + " person=" + v);
        }
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

  public List<Person> getVisitors()
  {
    return visitors;
  }
}
