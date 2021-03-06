package de.bitnoise.sonferenz.web.pages.config;

import org.apache.wicket.markup.html.panel.Panel;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.app.Right;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.UnauthorizedPanel;

@At(url = "/admin/text")
public class EditTextePage extends KonferenzPage
{
  @Override
  protected Panel getPageContent(String id)
  {
    if(KonferenzSession.hasRight(Right.Admin.Configure)) {
      return new ListTextePanel(id);
    } else {
      return new UnauthorizedPanel(id);
    }
  }
}
