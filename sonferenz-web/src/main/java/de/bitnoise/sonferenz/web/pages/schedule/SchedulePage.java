package de.bitnoise.sonferenz.web.pages.schedule;

import org.apache.wicket.markup.html.panel.Panel;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.web.pages.KonferenzPage;

public class SchedulePage extends KonferenzPage
{
  @Override
  protected Panel getPageContent(String id)
  {
      return new SchedulePanel(id);
  }
  
}
