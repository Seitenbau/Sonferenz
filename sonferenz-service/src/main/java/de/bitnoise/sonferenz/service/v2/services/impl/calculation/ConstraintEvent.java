package de.bitnoise.sonferenz.service.v2.services.impl.calculation;

public class ConstraintEvent
{

  protected String _message;

  protected Integer _weight;

  public ConstraintEvent(String message, Integer weight)
  {
    _message = message;
    _weight = weight;
  }

  public Integer getWeight()
  {
    return _weight;
  }

  @Override
  public String toString()
  {
    return _message + " weight=" + _weight;
  }

  public static ConstraintEvent impossible(String message)
  {
    return new ConstraintEvent(message, -1);
  }

  public static ConstraintEvent possible(String message, Integer weight)
  {
    return new ConstraintEvent(message, weight);
  }

}
