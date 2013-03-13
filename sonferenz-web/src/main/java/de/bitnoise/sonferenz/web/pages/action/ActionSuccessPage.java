package de.bitnoise.sonferenz.web.pages.action;

import org.apache.wicket.markup.html.panel.Panel;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.StaticContentPanel;
@At(url = "/actionsuccess")
public class ActionSuccessPage extends KonferenzPage
{
  @Override
  protected Panel getPageContent(String id)
  {
    return new StaticContentPanel(id, "action.general.success");
  }

}
