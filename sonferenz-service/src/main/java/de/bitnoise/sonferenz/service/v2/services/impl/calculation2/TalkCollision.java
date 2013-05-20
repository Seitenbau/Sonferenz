package de.bitnoise.sonferenz.service.v2.services.impl.calculation2;

import java.util.ArrayList;
import java.util.List;

public class TalkCollision
{
  List<Person> colliders;
  Talk talkA;
  Talk talkB;
  boolean possible;
  int weight;
  List<WeightedVisit> weights = new ArrayList<WeightedVisit>();

  public TalkCollision(Talk a, Talk b, boolean notPossible, List<Person> collidedPersons)
  {
    talkA = a;
    talkB = b;
    colliders = collidedPersons;
    possible = !notPossible;
    weight = 0;
  }

  public void addWeight(WeightedVisit wv)
  {
    weight += wv.getWeight();
    weights.add(wv);
  }

  List<WeightedVisit> getWeights()
  {
    return weights;
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

  public Integer getWeight()
  {
    return weight;
  }

}
