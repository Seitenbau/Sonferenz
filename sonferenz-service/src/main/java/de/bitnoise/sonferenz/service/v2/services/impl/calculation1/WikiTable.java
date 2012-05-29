package de.bitnoise.sonferenz.service.v2.services.impl.calculation1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class WikiTable
{
  private static final char SEPARATOR = '|';
  
  private List<String> headline = new ArrayList<String>();

  private List<List<String>> rows = new ArrayList<List<String>>();
  
  public List<List<String>> getRows()
  {
    return rows;
  }
  
  public void read(String wikiMarkup)
  {
    ParseStatus parseStatus = new ParseStatus(wikiMarkup);
    while (true) {
      StringBuilder lineBuilder = new StringBuilder();
      int nextLineEnd;
      String fragment;
      do
      {
        nextLineEnd 
            = wikiMarkup.indexOf("\r\n", parseStatus.getPosition());
        if (nextLineEnd == -1)
        {
          fragment = wikiMarkup.substring(parseStatus.getPosition());
          parseStatus.setPosition(wikiMarkup.length());
        }
        else
        {
          fragment = wikiMarkup.substring(parseStatus.getPosition(), nextLineEnd);
          parseStatus.setPosition(nextLineEnd + 2);
        }
        lineBuilder.append(fragment);
      }
      while (fragment.endsWith("\\\\") || 
          (!parseStatus.isAtEnd(parseStatus.getPosition()) 
              && wikiMarkup.charAt(parseStatus.getPosition()) == '#'));
      
      String line = lineBuilder.toString().trim();
            
      String[] columns = StringUtils.split(line, "|");
      List<String> row = Arrays.asList(columns);
      rows.add(row);

      if (nextLineEnd == -1)
      {
        break;
      }
    }
  }
  
  public List<String> getColumn(int index)
  {
    List<String> result = new ArrayList<String>(rows.size());
    for (List<String> row : rows)
    {
      if (row.size() > index)
      {
        result.add(row.get(index));
      }
      else
      {
        result.add(null);
      }
    }
    return result;
  }
  
  private static class ParseStatus
  {
    private String toParse;
    
    private boolean inHeadline = true;

    private int position = 0;
    
    
    public ParseStatus(String toParse)
    {
      this.toParse = toParse;
    }
    
    /**
     * @return the toParse
     */
    public String getToParse()
    {
      return toParse;
    }
    
    /**
     * @return the position
     */
    public int getPosition()
    {
      return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(int position)
    {
      this.position = position;
    }

    /**
     * @return the inHeadline
     */
    public boolean isInHeadline()
    {
      return inHeadline;
    }

    /**
     * @param inHeadline the inHeadline to set
     */
    public void setInHeadline(boolean inHeadline)
    {
      this.inHeadline = inHeadline;
    }
    
    public boolean isAtEnd(int parsePosition) {
      return parsePosition >= toParse.length();
    }
  }
}
