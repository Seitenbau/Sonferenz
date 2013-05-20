package de.bitnoise.sonferenz.service.v2.services.impl.calculation2;

import java.math.BigDecimal;
import java.util.List;

public class TalkCollision
{
  List<Person> colliders;
  Talk talkA;
  Talk talkB;
  boolean possible;

  public TalkCollision(Talk a, Talk b, boolean notPossible, List<Person> collidedPersons)
  {
    talkA = a;
    talkB = b;
    colliders = collidedPersons;
    possible = !notPossible;
  }

  public List<Person> getColliders()
  {
    return colliders;
  }

  public Talk getTalkA()
  {
    return talkA;
  }

  public Talk getTalkB()
  {
    return talkB;
  }

  boolean isPossible()
  {
    return possible;
  }

}
