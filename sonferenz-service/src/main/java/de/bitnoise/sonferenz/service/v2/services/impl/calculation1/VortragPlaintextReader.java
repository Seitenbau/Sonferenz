package de.bitnoise.sonferenz.service.v2.services.impl.calculation1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class VortragPlaintextReader
{
  private static final String SOURCE = "/nov2010.txt";
  

  public List<Vortrag> read() throws IOException
  {
    InputStream inputStream = VortragWikiReader.class.getResourceAsStream(SOURCE);
    BufferedReader bufferedReader 
      = new BufferedReader(new InputStreamReader(inputStream, "ISO-8859-1"));
   
    List<Vortrag> vortragList = new ArrayList<Vortrag>();
    
    // Vortr�ge einlesen
    String line = bufferedReader.readLine();
    while (line != null && !StringUtils.isEmpty(line))
    {
      int colonPos = line.indexOf(":");
      String idUndVortragende = line.substring(0, colonPos);
      String titel = line.substring(colonPos + 1);
      
      int firstSpacePos = idUndVortragende.indexOf(" ");
      String id = idUndVortragende.substring(0, firstSpacePos);
      String referentenString = idUndVortragende.substring(firstSpacePos + 1);
      
      Vortrag vortrag = new Vortrag(id);
      vortrag.setTitel(titel);
      
      String[] referenten = StringUtils.split(referentenString, ",");
      for (String referent : referenten)
      {
        vortrag.getReferenten().add(referent.replace(":", ""));
      }
      vortragList.add(vortrag);

      System.out.println("Vortrag gelesen: " + vortrag + " Titel : " + titel);
      
      line = bufferedReader.readLine();
    }
    
    // Teilnehmer einlesen
    line = bufferedReader.readLine();
    while (line != null)
    {
      int firstSpacePos = line.indexOf(" ");
      String teilnehmer = line.substring(0, firstSpacePos);
      teilnehmer = teilnehmer.replace(":", "");
      String vortragIdsString = line.substring(firstSpacePos + 1);
      String[] vortragIds = StringUtils.split(vortragIdsString, ",");
      for (String vortragId : vortragIds)
      {
        vortragId = vortragId.trim();
        boolean found = false;
        for (Vortrag vortrag : vortragList)
        {
          if (vortrag.getId().equals(vortragId))
          {
            vortrag.getTeilnehmer().add(teilnehmer);
            found = true;
            break;
          }
        }
        if (!found)
        {
          throw new RuntimeException("Vortrag mit Id " 
              + vortragId + " nihct gefunden");
        }
      }
      
      line = bufferedReader.readLine();
    }
    bufferedReader.close();
    
    System.out.println("Lesen beendet");
    
    return vortragList;
  }
}
