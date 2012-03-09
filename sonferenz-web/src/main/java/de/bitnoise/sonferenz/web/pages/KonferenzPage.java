package de.bitnoise.sonferenz.web.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import de.bitnoise.sonferenz.KonferenzSession;
import de.bitnoise.sonferenz.web.ConfigMainNavigation;
import de.bitnoise.sonferenz.web.component.confpan.CurrentConferencePanel;
import de.bitnoise.sonferenz.web.component.navigation.NavCallbackInterface;
import de.bitnoise.sonferenz.web.component.navigation.NavPanel;
import de.bitnoise.sonferenz.web.component.user.CurrentUserPanel;
import de.bitnoise.sonferenz.web.forms.KonferenzForm;
import de.bitnoise.sonferenz.web.pages.auth.LoginPanel;

public class KonferenzPage extends WebPage
{
  private KonferenzForm _form;

  public KonferenzPage()
  {
    super();
  }

  public KonferenzPage(PageParameters params)
  {
    super(params);
  }

  public KonferenzPage(KonferenzForm form)
  {
    _form = form;
  }

  @Override
  protected void onInitialize()
  {
    super.onInitialize();
//    add(new StyleSheetReference("stylesheet", KonferenzPage.class, "style.less"));
    //add(new StyleSheetReference("stylesheet", KonferenzPage.class, "/styles.css"));

    IModel<NavCallbackInterface> callbackModel = new LoadableDetachableModel<NavCallbackInterface>()
    {
      @Override
      protected NavCallbackInterface load()
      {
        return ConfigMainNavigation.getMainNaviagtion();
      }
    };
    
		if (KonferenzSession.noUserLoggedIn()) {
			add(new LoginPanel("nav"));
		} else {
			add(new NavPanel("nav", callbackModel));
		}
//    add(new CurrentUserPanel("currentUser"));
    add(new Label("footerText", de.bitnoise.sonferenz.Version.VERSION));
    add(new CurrentConferencePanel("currentConference"));
    add(getPageContent("pageContent"));
  }

  protected Panel getPageContent(String id)
  {
    if(_form== null) {
      return new StaticContentPanel(id, "page.default");
    } else {
      Panel panel = _form.createPanel(id);
      return panel;
    }
  }

  public List<NavCallbackInterface> getNavigations()
  {
    ArrayList<NavCallbackInterface> navs = new ArrayList<NavCallbackInterface>();
    buildNavigation(navs);
    return navs;
  }

  protected void buildNavigation(ArrayList<NavCallbackInterface> navs)
  {

  }

  public Object getCurrentAction()
  {
    return null;
  }

}
