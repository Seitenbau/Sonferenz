package de.bitnoise.sonferenz.web.pages.whish.action;


import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;

import de.bitnoise.sonferenz.KonferenzSession;
import de.bitnoise.sonferenz.model.WhishModel;
import de.bitnoise.sonferenz.web.action.ActionBookmark;
import de.bitnoise.sonferenz.web.action.IWebAction;
import de.bitnoise.sonferenz.web.action.WebAction;
import de.bitnoise.sonferenz.web.pages.auth.LoginPage;
import de.bitnoise.sonferenz.web.pages.whish.ModelWhishList;
import de.bitnoise.sonferenz.web.pages.whish.ReftoWhish;
import de.bitnoise.sonferenz.web.pages.whish.ViewWhishPage;
import de.bitnoise.sonferenz.web.pages.whish.WhishOverviewPage;

public class EditOrViewWhish extends WebAction<IModel<Object>> implements
    ActionBookmark<Object>
{

  public Page doAction(IModel<Object> model)
  {
    Object obj = model.getObject();
    WhishModel whish = null;
    if (obj instanceof ReftoWhish)
    {
      whish = ((ReftoWhish) obj).getWhish();
    }
    if (obj instanceof WhishModel)
    {
      whish = ((WhishModel) obj);
    }
    if (whish != null)
    {
      if (KonferenzSession.noUserLoggedIn())
      {
        return new LoginPage((IWebAction) this, model);
      }
      WhishOverviewPage userOverviewPage = new WhishOverviewPage();
      if (KonferenzSession.isUser(whish.getOwner())
          || KonferenzSession.isAdmin())
      {
        userOverviewPage.editTalk(whish);
      }
      else
      {
        userOverviewPage.viewTalk(whish);
      }
      return userOverviewPage;
    }
    return new WhishOverviewPage();
  }

  public Link createBookmark(IModel<Object> model, String id)
  {
    Object obj = model.getObject();
    WhishModel whish = null;
    if (obj instanceof ReftoWhish) {
    	whish = ((ReftoWhish) obj).getWhish();
    } else if (obj instanceof ModelWhishList)
    {
      whish = ((ModelWhishList) obj).whish;
    }
    if (whish != null)
    {
      Integer whishId = whish.getId();
      PageParameters parameter = new PageParameters();
      parameter.add(ViewWhishPage.PARAM_ID, "" + whishId);
      return new BookmarkablePageLink(id, ViewWhishPage.class, parameter);
    }
    return null;
  }

}
