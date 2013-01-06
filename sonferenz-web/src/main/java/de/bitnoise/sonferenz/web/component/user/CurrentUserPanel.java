/*****************************************
 * Quelltexte zum Buch: Praxisbuch Wicket
 * (http://www.hanser.de/978-3-446-41909-4)
 * 
 * Autor: Michael Mosmann (michael@mosmann.de)
 *****************************************/
package de.bitnoise.sonferenz.web.component.user;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.context.ApplicationContext;

import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.service.v2.exceptions.GeneralConferenceException;
import de.bitnoise.sonferenz.service.v2.services.AuthenticationService;
import de.bitnoise.sonferenz.web.pages.auth.LoginPage;
import de.bitnoise.sonferenz.web.pages.auth.LogoutPage;

public class CurrentUserPanel extends Panel  
{
  public CurrentUserPanel(String id)
  {
    super(id);
    InjectorHolder.getInjector().inject(this);
    initPanel();
  }

  @SpringBean
  AuthenticationService auth;

  private ApplicationContext ctx;

  protected void initPanel()
  {
    try
    {
      UserModel user = auth.getCurrentUser();
      String username = "Login";
      Class<? extends WebPage> target;
      if (user != null)
      {
        createContent(user.getName(), LogoutPage.class);
      }
      else
      {
        createContent(null, LoginPage.class);
      }
    }
    catch (GeneralConferenceException e)
    {
      e.printStackTrace();
    }
  }

  private void createContent(String userName,
      Class<? extends WebPage> linkTarget)
  {
    String prefix = getString("toolbar.user." + linkTarget.getSimpleName()
        + ".prefix", Model.of(userName));
    String label = getString("toolbar.user." + linkTarget.getSimpleName()
        + ".label",
        Model.of(userName));
    BookmarkablePageLink<WebPage> link = new BookmarkablePageLink<WebPage>(
        "link", linkTarget);
    add(link);
    add(new Label("prefixText", Model.of(prefix)));
    link.add(new Label("name", Model.of(label)));
  }
}
