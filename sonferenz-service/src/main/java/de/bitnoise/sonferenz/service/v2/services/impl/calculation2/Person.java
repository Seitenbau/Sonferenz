package de.bitnoise.sonferenz.service.v2.services.impl.calculation2;

public class Person
{

  Object id;

  public Person(Object id)
  {
    this.id = id;
  }

  @Override
  public String toString()
  {
    return id.toString();
  }

  public Object getId()
  {
    return id;
  }
}
