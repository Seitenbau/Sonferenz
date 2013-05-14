package de.bitnoise.sonferenz.service.v2.services.impl.calculation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CalcTalk;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CollisionResult;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.Collisions;
import de.bitnoise.sonferenz.service.v2.services.impl.calculation1.Calculate;
import de.bitnoise.sonferenz.service.v2.services.impl.calculation1.Vortrag;
import de.bitnoise.sonferenz.service.v2.services.impl.calculation1.Calculate.VortragPair;

public class CalculateSlots
{

  protected CalculationConfigurationImpl _config;

  public CalculateSlots(CalculationConfigurationImpl theConfig)
  {
    _config = theConfig;
  }

  public SlotOrdernImpl calculate(CollisionResult collisions)
  {
    ConferenceSlots slotconfig = _config.getSlotConfig();

    List<SlotReference> slots = slotconfig.getAllSlots();
    List<CalculationTalkImpl> talks = _config.getTalks();

    int max = talks.size() * talks.size();
    List<SlotReference> result = createPair(collisions,slots,talks);
    Integer count = countCollisions(collisions, result);
    System.out.println("final collision count = " + count);
    System.out.println(max);
    return new SlotOrdernImpl(result,count);
  }

  public List<SlotReference> createPair(
      CollisionResult collisions,
      Collection<SlotReference> slots,
      Collection<CalculationTalkImpl> talks)
  {
    List<SlotReference> result = null;
    if (slots.size() == 0)
    {
      return new ArrayList<SlotReference>();
    }
    SlotReference slot = slots.iterator().next();
    Integer minCollisions = Integer.MAX_VALUE;
    for (CalculationTalkImpl vortrag1 : talks)
    {
      for (CalculationTalkImpl vortrag2 : talks)
      {
        if (vortrag1 == vortrag2)
        {
          break;
        }
        Collection<SlotReference> remainingSlots = new ArrayList<SlotReference>(slots);
        remainingSlots.remove(slot);
        Collection<CalculationTalkImpl> remaining = new ArrayList<CalculationTalkImpl>(talks);
        remaining.remove(vortrag1);
        remaining.remove(vortrag2);
        List<SlotReference> pairings = createPair(collisions, remainingSlots,remaining);
        if (pairings == null)
        {
          continue;
        }
        Integer count = countCollisions(collisions, pairings);
        Collisions here = collisions.getCollisions(vortrag1, vortrag2);
        if (here != null)
        {
          if (!here.isPossible())
          {
            continue;
          }
          count += here.getStrength();
        }
        if (count == null)
        {
          continue;
        }
        if (count < minCollisions)
        {
          minCollisions = count;
          result = pairings;
          SlotReference t = new SlotReference();
          t.add(vortrag1);
          t.add(vortrag2);
          result.add(t);
          // bestPairings = pairings;
          // bestPairings.add(new VortragPair(vortrag1, vortrag2));
        }
      }
    }
    return result;
  }

  private Integer countCollisions(CollisionResult collisions,
      List<SlotReference> pairings)
  {
    int s = 0;
    for (SlotReference slots : pairings)
    {
      Collisions col = readCollision(collisions, slots);
      if (col == null)
      {
        continue;
      }
      if (!col.isPossible())
      {
        return null;
      }
      s += col.getStrength();
    }
    return s;
  }

  public Collisions readCollision(CollisionResult collisions,
      SlotReference slots)
  {
    CalcTalk talkA = slots.getTalk(0);
    CalcTalk talkB = slots.getTalk(1);
    Collisions col = collisions.getCollisions(talkA, talkB);
    return col;
  }

  protected SlotReference getPairingFromCollection(
      Collection<CalculationTalkImpl> talks)
  {
    SlotReference pairing = new SlotReference();
    for (CalculationTalkImpl vortrag : talks)
    {
      pairing.add(vortrag);
    }
    return pairing;
  }

  // protected boolean reachedFinalPermutation(
  // ResetableIterator<CalculationTalkImpl>[] slotItem)
  // {
  // boolean finished = true;
  // for (Iterator<CalculationTalkImpl> iter : slotItem)
  // {
  // if (iter.hasNext())
  // {
  // finished = false;
  // break;
  // }
  // }
  // return finished;
  // }

}
