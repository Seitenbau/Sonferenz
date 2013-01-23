package de.bitnoise.sonferenz.web.pages.log;

import java.io.StringWriter;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.html.HTMLLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.read.CyclicBufferAppender;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.web.app.KonferenzSession;

@At(url = "/log")
public class LoggingPage extends WebPage
{
  private static final String USERID_MDC_KEY = "MDC";

  private static final String CYCLIC_BUFFER_APPENDER_NAME = "CYCLIC";

  Logger logger = LoggerFactory.getLogger(LoggingPage.class);

  HTMLLayout layout;

  static String PATTERN = "%d%thread%level%logger{25}%mdc{"
      + USERID_MDC_KEY + "}%msg";

  public LoggingPage()
  {
    LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
    initialize(lc);
  }

  @Override
  protected void onInitialize()
  {
    super.onInitialize();

    LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
    CyclicBufferAppender<ILoggingEvent> cyclicBufferAppender = getCyclicBuffer(context);

    String content = "";
    if (KonferenzSession.get().isAdmin())
    {
      content = content(cyclicBufferAppender);
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

  private String content(
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

  void initialize(LoggerContext context)
  {
    layout = new HTMLLayout();
    layout.setContext(context);
    layout.setPattern(PATTERN);
    layout.setTitle("Last Logging Events");
    layout.start();
  }

}
