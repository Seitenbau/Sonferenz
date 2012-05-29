package de.bitnoise.sonferenz.service.v2.services.impl.calculation1;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Vortrag
{
  private String id;
  
  private String titel;
  
  private Set<String> referenten = new TreeSet<String>();
  
  private Set<String> teilnehmer = new TreeSet<String>();

  public Vortrag(String id)
  {
    this.id = id;
  }
  
  /**
   * @return the id
   */
  public String getId()
  {
    return id;
  }

  /**
   * @return the titel
   */
  public String getTitel()
  {
    return titel;
  }

  /**
   * @param titel the titel to set
   */
  public void setTitel(String titel)
  {
    this.titel = titel;
  }

  /**
   * @return the referent
   */
  public Set<String> getReferenten()
  {
    return referenten;
  }

  /**
   * @return the teilnehmer
   */
  public Set<String> getTeilnehmer()
  {
    return teilnehmer;
  }

  @Override
  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    builder.append("Vortrag [id=");
    builder.append(id);
    builder.append(", referenten=");
    builder.append(referenten);
    builder.append("]");
    return builder.toString();
  }
  
  public int kollisionen(Vortrag other)
  {
    int result = 0;
    Set<String> teilnehmerUndReferentenSelbst = new HashSet<String>();
    teilnehmerUndReferentenSelbst.addAll(teilnehmer);
    teilnehmerUndReferentenSelbst.addAll(referenten);
    Set<String> teilnehmerUndReferentenOther = new HashSet<String>();
    teilnehmerUndReferentenOther.addAll(other.teilnehmer);
    teilnehmerUndReferentenOther.addAll(other.referenten);

    for (String person : teilnehmerUndReferentenSelbst)
    {
      if (teilnehmerUndReferentenOther.contains(person))
      {
        result ++;
      }
    }
    return result;
  }
  
  public static class VortragTeilnehmerComparator implements Comparator<Vortrag>
  {
    public int compare(Vortrag vortrag1, Vortrag vortrag2)
    {
      return vortrag2.getTeilnehmer().size() - vortrag1.getTeilnehmer().size();
    }
  }

  public Integer getVotes()
  {
    return (teilnehmer==null ? 0 : teilnehmer.size());
  }
}
