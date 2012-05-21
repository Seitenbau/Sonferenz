package de.bitnoise.sonferenz.web.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.web.ConfigMainNavigation;
import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.component.confpan.CurrentConferencePanel;
import de.bitnoise.sonferenz.web.component.footer.FooterPanel;
import de.bitnoise.sonferenz.web.component.lvl1.FirstLevelBar;
import de.bitnoise.sonferenz.web.component.lvl2.SecondLevelBar;
import de.bitnoise.sonferenz.web.component.navigation.NavCallbackInterface;
import de.bitnoise.sonferenz.web.component.navigation.NavPanel;
import de.bitnoise.sonferenz.web.component.user.CurrentUserPanel;
import de.bitnoise.sonferenz.web.forms.KonferenzForm;
import de.bitnoise.sonferenz.web.pages.auth.LoginPanel;

public abstract class KonferenzPage extends WebPage
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

    add(new SecondLevelBar("nav"));
    add(new FirstLevelBar("firstLevelBar"));
//		if (KonferenzSession.noUserLoggedIn()) {
//			add(new LoginPanel("nav"));
//		} else {
//			add(new NavPanel("nav", callbackModel));
//		}
//    add(new CurrentUserPanel("currentUser"));
    add(new FooterPanel("footer"));
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

	public  static IModel<String> txt(String id) {
		return new StringResourceModel( id, Model.of(id));
	}

}
