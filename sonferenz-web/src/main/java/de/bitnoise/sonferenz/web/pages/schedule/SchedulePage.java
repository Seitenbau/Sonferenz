package de.bitnoise.sonferenz.web.pages.schedule;

import org.apache.wicket.markup.html.panel.Panel;

import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.UnauthorizedPanel;

public class SchedulePage extends KonferenzPage
{
  @Override
  protected Panel getPageContent(String id)
  {
    if(KonferenzSession.noUserLoggedIn()) {
      return new UnauthorizedPanel(id);
    } else {
      return new SchedulePanel(id);
    }
  }
  
}
