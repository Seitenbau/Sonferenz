package de.bitnoise.sonferenz.web.pages.action;

import java.util.Map;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.visural.wicket.aturl.At;
import com.visural.wicket.aturl.URLType;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.service.v2.actions.ActionState;
import de.bitnoise.sonferenz.service.v2.actions.Aktion;
import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.app.Right;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.UnauthorizedPanel;
import de.bitnoise.sonferenz.web.pages.conference.EditConferenceWizard;
import de.bitnoise.sonferenz.web.pages.conference.ListConferencesPanel;

@At(url = "/action", type = URLType.IndexedStateInURL, urlParameters = "token")
public class ActionPage extends KonferenzPage
{
  String action;

  String token;

  @SpringBean
  UiFacade facade;

  public ActionPage(PageParameters params)
  {
    super(params);
    if (params.size() == 3)
    {
      if ("token".equalsIgnoreCase(params.getString("1", null)))
      {
        action = params.getString("0", null);
        token = params.getString("2", null);
      }
    }
  }

  @Override
  protected Panel getPageContent(String id)
  {
    Panel panel = validate(id, action, token);
    if (panel == null)
    {
      return new UnauthorizedPanel(id);
    }
    return panel;
  }

  Panel validate(String id, String action, String token)
  {
    Aktion aktion = facade.validateAction(action, token);
    if (aktion == null)
    {
      return null;
    }
    if ("subscribe".equals(aktion.getAction()))
    {
      return new SubscribeActionPanel(id,aktion);
    }
    return null;
  }
}
