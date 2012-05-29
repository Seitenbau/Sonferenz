package de.bitnoise.sonferenz.service.v2.services.impl.calculation;

import java.util.HashMap;
import java.util.Map;

import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CalcUser;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.ConstraintFunction;

public class ConstraintFuncs
{
  protected static Map<Integer, ConstraintFunction> cache = new HashMap<Integer, ConstraintFunction>();

  public static ConstraintFunction visit(final Integer level)
  {
    ConstraintFunction func = cache.get(level);
    if (func != null)
    {
      return func;
    }
    func = new ConstraintFunction()
    {
      @Override
      public ConstraintEvent calculate(CalculationTalkImpl talkB, CalcUser user)
      {
        if (talkB.containsUser(user))
        {
          return ConstraintEvent.possible("user " + user + " also visits " + talkB,level);
        }
        return null;
      }

      @Override
      public String toString()
      {
        return "visit-constraint(" + level + ")";
      }
    };
    cache.put(level, func);
    return func;
  }

  public static final ConstraintFunction speaker()
  {
    return new ConstraintFunction()
    {
      @Override
      public ConstraintEvent calculate(CalculationTalkImpl talkB, CalcUser user)
      {
        if (talkB.containsSpeaker(user))
        {
          return ConstraintEvent.impossible("talk " + talkB + " contains speaker " + user);
        }
        return null;
      }

      @Override
      public String toString()
      {
        return "speaker-constraint";
      }
    };
  }
}
