package de.bitnoise.sonferenz.web.pages.schedule;

import org.apache.wicket.markup.html.panel.Panel;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.UnauthorizedPanel;

@At(url="/timetable")
public class NewSchedulePage extends KonferenzPage
{
  @Override
  protected Panel getPageContent(String id)
  {
      return new NewSchedulePanel(id);
  }
}
