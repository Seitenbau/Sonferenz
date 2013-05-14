package de.bitnoise.sonferenz.web.pages.statics;

import org.apache.wicket.markup.html.panel.Panel;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.StaticContentPanel;
import de.bitnoise.sonferenz.web.pages.UnauthorizedPanel;

@At(url = "/schedule")
public class SchedulePage extends KonferenzPage
{
  @Override
  protected Panel getPageContent(String id)
  {
    Integer cid = KonferenzSession.get().getCurrentConference().getId();
    if(cid!=1) {
      return new StaticContentPanel(id, "page.schedule." + cid);
    }
    return new StaticContentPanel(id, "page.schedule");
  }

}
