package de.bitnoise.sonferenz.web.pages.talks.action;

import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;


import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.web.action.ActionBookmark;
import de.bitnoise.sonferenz.web.action.IWebAction;
import de.bitnoise.sonferenz.web.action.WebAction;
import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.pages.auth.LoginPage;
import de.bitnoise.sonferenz.web.pages.error.UnauthorisedAccess;
import de.bitnoise.sonferenz.web.pages.talks.ModelTalkList;
import de.bitnoise.sonferenz.web.pages.talks.RefToTalk;
import de.bitnoise.sonferenz.web.pages.talks.TalksOverviewPage;
import de.bitnoise.sonferenz.web.pages.talks.ViewTalkPage;
import de.bitnoise.sonferenz.web.pages.voting.UserVoteItem;

public class EditOrViewTalk extends WebAction<IModel<Object>>
// implements AclControlled
    implements ActionBookmark<Object>
{

  public Page doAction(IModel<Object> model)
  {
    Object obj = model.getObject();
    TalkModel talk = null;
    if (obj instanceof ModelTalkList )
    {
      talk = ((ModelTalkList) obj).talk;
    }
    else if (obj instanceof TalkModel)
    {
      talk = ((TalkModel) obj);
    }
    if (talk != null)
    {
      TalksOverviewPage userOverviewPage = new TalksOverviewPage();
      userOverviewPage.viewTalk(talk);
//      if (KonferenzSession.isUser(talk.getOwner())
//          || KonferenzSession.isAdmin())
//      {
//        userOverviewPage.viewTalk(talk);
//      }
//      else
//      {
//        userOverviewPage.viewTalk(talk);
//      }
      return userOverviewPage;
    }
    return new TalksOverviewPage();
  }

  public Link<Object> createBookmark(IModel<Object> model, String id)
  {
    Object obj = model.getObject();
    TalkModel talk = null;
    if (obj instanceof RefToTalk)
    {
      talk = ((RefToTalk) obj).getTalk();
    }
    if (talk != null)
    {
      PageParameters param = new PageParameters();
      param.add(ViewTalkPage.PARAM_ID, "" + talk.getId());
      return new BookmarkablePageLink(id, ViewTalkPage.class, param);
    }
    else
    {
      return null;
    }
  }

}
