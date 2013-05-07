package de.bitnoise.sonferenz.web.pages.action;

import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.visural.wicket.aturl.At;
import com.visural.wicket.aturl.URLType;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.service.v2.actions.Aktion;
import de.bitnoise.sonferenz.service.v2.actions.impl.ChangePasswordActionImpl.ChangePasswordOfUser;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.UnauthorizedPanel;

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
    if (params.getNamedKeys().size() == 3)
    {
      if ("token".equalsIgnoreCase(params.get("1").toString(null)))
      {
        action = params.get("0").toString(null);
        token = params.get("2").toString(null);
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
    if ("changePassword".equals(aktion.getAction()))
    {
    	ChangePasswordOfUser data = (ChangePasswordOfUser)aktion.getData();
    	data.setTokenId(aktion.getId());
		facade.executeAction(data);
    	setResponsePage(ActionSuccessPage.class);
    	return new EmptyPanel(id);
    }
    return null;
  }
}
