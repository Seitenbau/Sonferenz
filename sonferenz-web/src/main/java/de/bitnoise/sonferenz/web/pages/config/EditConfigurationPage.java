package de.bitnoise.sonferenz.web.pages.config;

import org.apache.wicket.markup.html.panel.Panel;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.app.Right;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.UnauthorizedPanel;

@At(url = "/admin/configuration")
public class EditConfigurationPage extends KonferenzPage
{
  @Override
  protected Panel getPageContent(String id)
  {
    if(KonferenzSession.hasRight(Right.Admin.Configure)) {
      return new ListConfigPanel(id);
    } else {
      return new UnauthorizedPanel(id);
    }
  }
}
