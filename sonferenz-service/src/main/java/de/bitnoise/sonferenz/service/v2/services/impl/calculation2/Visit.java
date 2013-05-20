package de.bitnoise.sonferenz.service.v2.services.impl.calculation2;

public class Visit
{

  Person person;
  int weight;

  public Visit(Person aPerson, int aWeight)
  {
    person = aPerson;
    weight = aWeight;
  }

  public Person getPerson()
  {
    return person;
  }

  public int getWeight()
  {
    return weight;
  }

}
