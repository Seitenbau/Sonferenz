package de.bitnoise.sonferenz.service.v2.services.impl.calculation1;

import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;

public class WikiFormat
{
  public static final char MACRO_START = '{';
  
  public static final char MACRO_END = '}';

  /**
   * Entfernt Macros. Escaping wird nicht beachtet !!!!
   * @param input
   * @return
   */
  public static final String removeMacros(String input)
  {
    StringBuilder resultBuilder = new StringBuilder();
    ParserState parserState = new ParserState(input);
    
    StringTokenizer stringTokenizer = new StringTokenizer(input, "\\{}", true);
    while (stringTokenizer.hasMoreElements())
    {
      String token = stringTokenizer.nextToken();
      if (parserState.isInEscape())
      {
        if ("\\".equals(token))
        {
          // Zeilenumbruch
          resultBuilder.append("\n");
        }
        else
        {
          resultBuilder.append(token);
        }
        parserState.setInEscape(false);
        continue;
      }
      if ("\\".equals(token))
      {
        parserState.setInEscape(true);
        continue;
      }
      if (!parserState.isInMacro())
      {
        if ("{".equals(token))
        {
          parserState.setInMacro(true);
          continue;
        }
        resultBuilder.append(token);
        continue;
      }
      if ("}".equals(token))
      {
        parserState.setInMacro(false);
      }
    }
    String result = resultBuilder.toString();
    result = StringUtils.replace(result, "&nbsp;", " ");
    return result.toString();
  }
  
  private static class ParserState {
    
    private String content;
    
    private ParserState(String content)
    {
      this.content = content;
    }
    
    /**
     * @return the parsePosition
     */
    public int getParsePosition()
    {
      return parsePosition;
    }

    /**
     * @param parsePosition the parsePosition to set
     */
    public void setParsePosition(int parsePosition)
    {
      this.parsePosition = parsePosition;
    }

    /**
     * @return the inEscape
     */
    public boolean isInEscape()
    {
      return inEscape;
    }

    /**
     * @param inEscape the inEscape to set
     */
    public void setInEscape(boolean inEscape)
    {
      this.inEscape = inEscape;
    }

    /**
     * @return the inMacro
     */
    public boolean isInMacro()
    {
      return inMacro;
    }

    /**
     * @param inMacro the inMacro to set
     */
    public void setInMacro(boolean inMacro)
    {
      this.inMacro = inMacro;
    }

    /**
     * @return the content
     */
    public String getContent()
    {
      return content;
    }

    private int parsePosition;
    
    private boolean inEscape;
    
    private boolean inMacro;
  }
}
