package de.bitnoise.sonferenz.web.pages.schedule.edit;

import org.apache.wicket.markup.html.panel.Panel;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.web.pages.KonferenzPage;

@At(url = "/schedule/edit")
public class ScheduleEditPage extends KonferenzPage
{
  @Override
  protected Panel getPageContent(String id)
  {
      return new ScheduleEditPanel(id);
  }
  
}
