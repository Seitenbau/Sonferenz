package de.bitnoise.sonferenz.web.pages.admin.tabs;

import static ch.qos.logback.core.CoreConstants.LINE_SEPARATOR;

import java.io.StringWriter;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.html.HTMLLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.html.CssBuilder;
import ch.qos.logback.core.read.CyclicBufferAppender;
import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.component.panels.KonferenzTabPanel;
import de.bitnoise.sonferenz.web.pages.log.LoggingPage;

public class LogOutputPanel extends KonferenzTabPanel
{
  private final class MyCSS implements CssBuilder {
	    @Override
		public void addCss(StringBuilder sbuf) {
				    sbuf.append("<style  type=\"text/css\">");
				    sbuf.append(LINE_SEPARATOR);
				    sbuf
				        .append("table { margin-left: 0em; margin-right: 0em; border-left: 2px solid #AAA; }");
				    sbuf.append(LINE_SEPARATOR);

				    sbuf.append("TR.even { background: #FFFFFF; }");
				    sbuf.append(LINE_SEPARATOR);

				    sbuf.append("TR.odd { background: #EAEAEA; }");
				    sbuf.append(LINE_SEPARATOR);

				    sbuf
				        .append("TR.warn TD.Level, TR.error TD.Level, TR.fatal TD.Level {font-weight: bold; color: #FF4040 }");
				    sbuf.append(CoreConstants.LINE_SEPARATOR);

				    sbuf
				        .append("TD { padding-right: 1ex; padding-left: 1ex; border-right: 2px solid #AAA; }");
				    sbuf.append(LINE_SEPARATOR);

				    sbuf
				        .append("TD.Time, TD.Date { text-align: right; font-family: courier, monospace; font-size: smaller; }");
				    sbuf.append(LINE_SEPARATOR);

				    sbuf.append("TD.Thread { text-align: left; }");
				    sbuf.append(LINE_SEPARATOR);

				    sbuf.append("TD.Level { text-align: right; }");
				    sbuf.append(LINE_SEPARATOR);

				    sbuf.append("TD.Logger { text-align: left; }");
				    sbuf.append(LINE_SEPARATOR);

				    sbuf
				        .append("TR.header { background: #596ED5; color: #FFF; font-weight: bold; font-size: larger; }");
				    sbuf.append(CoreConstants.LINE_SEPARATOR);

				    sbuf
				        .append("TD.Exception { background: #A2AEE8; font-family: courier, monospace;}");
				    sbuf.append(LINE_SEPARATOR);

				    sbuf.append("</style>");
				    sbuf.append(LINE_SEPARATOR);
		}
    }

public LogOutputPanel(String id)
  {
    super(id);
  }

  private static final String USERID_MDC_KEY = "MDC";

  private static final String CYCLIC_BUFFER_APPENDER_NAME = "CYCLIC";

  Logger logger = LoggerFactory.getLogger(LoggingPage.class);
  
  static String PATTERN = "%d%thread%level%logger{25}%msg";

  @Override
  protected void onInitialize()
  {
    super.onInitialize();
    
    LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
    HTMLLayout layout = new HTMLLayout();
    layout.setContext(lc);
    layout.setPattern(PATTERN);
    layout.setCssBuilder(new MyCSS());
//    layout.setTitle("Last Logging Events");
    layout.start();

    LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
    CyclicBufferAppender<ILoggingEvent> cyclicBufferAppender = getCyclicBuffer(context);

    String content = "";
    if (KonferenzSession.get().isAdmin())
    {
      content = content(layout,cyclicBufferAppender);
    }
    Component label = new Label("text", content)
        .setEscapeModelStrings(false);
    add(label);
  }

  public CyclicBufferAppender<ILoggingEvent> getCyclicBuffer(
      LoggerContext context)
  {
    CyclicBufferAppender<ILoggingEvent> cyclicBufferAppender = (CyclicBufferAppender<ILoggingEvent>) context
        .getLogger(Logger.ROOT_LOGGER_NAME).getAppender(
            CYCLIC_BUFFER_APPENDER_NAME);
    return cyclicBufferAppender;
  }

  private String content(HTMLLayout layout,
      CyclicBufferAppender<ILoggingEvent> cyclicBufferAppender)
  {
    StringWriter output = new StringWriter();
    int count = -1;
    if (cyclicBufferAppender != null)
    {
      count = cyclicBufferAppender.getLength();
    }
    if (count == -1)
    {
      output.append("<tr><td>Failed to locate CyclicBuffer</td></tr>\r\n");
    }
    else if (count == 0)
    {
      output.append("<tr><td>No logging events to display</td></tr>\r\n");
    }
    else
    {
      output.append(layout.getFileHeader());
      output.append(layout.getPresentationHeader());
      LoggingEvent le;
      for (int i = 0; i < count; i++)
      {
        le = (LoggingEvent) cyclicBufferAppender.get(i);
        output.append(layout.doLayout(le) + "\r\n");
      }
      output.append(layout.getPresentationFooter());
      output.append(layout.getFileFooter());
    }
    return output.toString();
  }

}
