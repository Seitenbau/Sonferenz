package de.bitnoise.sonferenz.service.v2.services.impl.calculation2;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Collisions
{
  Map<Talk, Map<Talk, TalkCollision>> collisions = new HashMap<Talk, Map<Talk, TalkCollision>>();

  public TalkCollision collision(Talk t1, Talk t2)
  {
    Map<Talk, TalkCollision> entry = collisions.get(t1);
    if (entry == null)
    {
      throw new IllegalStateException("unknonwn Talk " + t1);
    }
    TalkCollision result = entry.get(t2);
    if (result == null)
    {
      throw new IllegalStateException("unknonwn result for Talk " + result);
    }
    return result;
  }

  public void add(TalkCollision col)
  {
    findMapFor(col.getTalkA()).put(col.getTalkB(), col);
    findMapFor(col.getTalkB()).put(col.getTalkA(), col);
  }

  Map<Talk, TalkCollision> findMapFor(Talk talk)
  {
    Map<Talk, TalkCollision> mapped = collisions.get(talk);
    if (mapped == null)
    {
      mapped = new HashMap<Talk, TalkCollision>();
      collisions.put(talk, mapped);
    }
    return mapped;
  }

}
