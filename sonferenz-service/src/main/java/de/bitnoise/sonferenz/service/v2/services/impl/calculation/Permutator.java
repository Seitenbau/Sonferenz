package de.bitnoise.sonferenz.service.v2.services.impl.calculation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Permutator<T>
{
  public abstract void onPermutation(Object[] data);

  class ResetableIterator<T>
  {

    Iterable<T> _iterable;

    Iterator<T> _iterator;

    private T _value;

    public ResetableIterator(Iterable<T> iterable)
    {
      _iterable = iterable;
      newIterator();
    }

    public Iterator<T> newIterator()
    {
      _iterator = _iterable.iterator();
      return _iterator;
    }

    public T next()
    {
      _value = _iterator.next();
      return _value;
    }

    public boolean hasNext()
    {
      return _iterator.hasNext();
    }

    public T value()
    {
      return _value;
    }
  }

  public void permutateFor(int count,
      List<T> talks)
  {
    ArrayList<ResetableIterator<T>> slotItem = new ArrayList<ResetableIterator<T>>();
    Object[] data = new Object[count];
    for (int i = 0; i < count; i++)
    {
      ResetableIterator e = new ResetableIterator(talks);
      e.next();
      data[i] = e.value();
      slotItem.add(e);
    }
    slotItem.get(0).newIterator();
    data[0] = null;
    permutate( slotItem, data);
  }

  protected void permutate( 
      ArrayList<ResetableIterator<T>> slotItem, Object[] data)
  {
    int i = 0;
    boolean finished = true;
    long start = System.currentTimeMillis();
    do
    {
      if (++i == 100000000)
      {
        long jetzt = System.currentTimeMillis();
        System.out.println(jetzt - start);
        start = jetzt;
        i = 0;
      }
      Iterator<ResetableIterator<T>> slotIter = slotItem.iterator();
      // increment 1.st position
      ResetableIterator<T> item = slotIter.next();
      finished = true;
      int idx = 0;
      do
      {
        if (item.hasNext())
        {
          item.next();
          data[idx] = item.value();
          finished = false;
        }
        else if (!slotIter.hasNext())
        {
          break;
        }
        else
        {
          item.newIterator();
          item.next();
          data[idx] = item.value();
          item = slotIter.next();
        }
        idx++;
      }
      while (finished);
      if (!finished)
      {
        preparePermutation(data);
      }
    }
    while (!finished);
  }

  protected void preparePermutation(Object[] data)
  {
    if (filter(data))
    {
      onPermutation(data);
    }
  }

  public boolean filter(Object[] data)
  {
    return true;
  }

}
