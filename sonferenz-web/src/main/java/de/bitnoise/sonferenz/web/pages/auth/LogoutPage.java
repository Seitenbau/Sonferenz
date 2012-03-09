package de.bitnoise.sonferenz.web.pages.auth;

import org.apache.wicket.markup.html.WebPage;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.web.pages.statics.ConferencePage;

@At(url="/logout")
public class LogoutPage extends WebPage
{
  public LogoutPage()
  {
    getSession().invalidate();
    setRedirect(true);
    setResponsePage(ConferencePage.class);
  }
}
