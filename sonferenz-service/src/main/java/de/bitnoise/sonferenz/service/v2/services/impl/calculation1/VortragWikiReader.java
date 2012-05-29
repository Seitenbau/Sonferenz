package de.bitnoise.sonferenz.service.v2.services.impl.calculation1;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

public class VortragWikiReader
{
  
  private static final String SOURCE = "/schulung2010.txt";
  
  private static final int ID_COLUMN = 0;

  private static final int TITEL_COLUMN = 2;

  private static final int REFERENT_COLUMN = 1;
  
  private static final int TEILNEHMER_COLUMN = 3;
  
  public List<Vortrag> read() throws IOException
  {
    InputStream inputStream = VortragWikiReader.class.getResourceAsStream(SOURCE);
    String content = IOUtils.toString(inputStream);
    int tableStart = content.indexOf("|");
    int tableEnd = content.lastIndexOf("|");
    WikiTable wikiTable = new WikiTable();
    wikiTable.read(content.substring(tableStart, tableEnd));
    
    List<Vortrag> vortragList = new ArrayList<Vortrag>();
    for (List<String> row : wikiTable.getRows())
    {
      if (row.size() <= TEILNEHMER_COLUMN)
      {
        continue;
      }
      String id = row.get(ID_COLUMN);
      id = WikiFormat.removeMacros(id);
      id = id.trim();
      Vortrag vortrag = new Vortrag(id);
      String referenten = row.get(REFERENT_COLUMN);
      vortrag.getReferenten().addAll(getPersonen(referenten));
      String teilnehmer = row.get(TEILNEHMER_COLUMN);
      vortrag.getTeilnehmer().addAll(getPersonen(teilnehmer));
      vortragList.add(vortrag);
    }
    return vortragList;
  }
  
  private Set<String> getPersonen(String column)
  {
    column.replace('\r', ' ');
    column.replace('\n', ' ');
    String[] teilnehmerArray 
        = StringUtils.splitByWholeSeparator(column, "\\\\");
    Set<String> teilnehmerSet = new HashSet<String>();
    for (String teilnehmer : teilnehmerArray)
    {
      teilnehmer = WikiFormat.removeMacros(teilnehmer);
      teilnehmer = teilnehmer.trim().toLowerCase();
      if (teilnehmer.endsWith("."))
      {
        teilnehmer = teilnehmer.substring(0, teilnehmer.length() -1);
      }
      if (StringUtils.isNotBlank(teilnehmer) && !teilnehmer.startsWith("-->"))
      {
        teilnehmerSet.add(teilnehmer.trim());
      }
    }
    return teilnehmerSet;
  }
}
