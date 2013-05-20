package de.bitnoise.sonferenz.service.v2.services.impl.calculation2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class TalksVisits implements Iterable<WeightedVisit>
{
  List<WeightedVisit> result = new ArrayList<WeightedVisit>();
  List<Person> persons = new ArrayList<Person>();

  public TalksVisits(Talk talk)
  {
    int base = 10;
    for (Visit v : talk.getVisits())
    {
      int userWeight = v.getWeight();
      int weight = base + userWeight;
      result.add(new WeightedVisit(v.getPerson(), weight));
      persons.add(v.getPerson());
    }
    for (Person p : talk.getSpeakers())
    {
      result.add(new WeightedVisit(p, base));
      persons.add(p);
    }
  }

  @Override
  public Iterator<WeightedVisit> iterator()
  {
    return result.iterator();
  }

  public boolean hasAPerson(WeightedVisit v)
  {
    return hasAPerson(v.getPerson());
  }

  public boolean hasAPerson(Person p)
  {
    return persons.contains(p);
  }
}
