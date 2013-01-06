package de.bitnoise.sonferenz.web.pages.error;


import org.apache.wicket.markup.html.panel.Panel;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.UnauthorizedPanel;
import de.bitnoise.sonferenz.web.pages.auth.LoginPage;
@At(url = "/unauthorisedAccess")
public class UnauthorisedAccess extends KonferenzPage
{

  @Override
  protected void onInitialize()
  {
    super.onInitialize();
    setResponsePage(LoginPage.class);
  }

  @Override
  protected Panel getPageContent(String id)
  {
    return new UnauthorizedPanel(id);
  }
}
