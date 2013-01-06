package de.bitnoise.sonferenz.web.pages.proposal.action;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import de.bitnoise.sonferenz.model.ProposalModel;
import de.bitnoise.sonferenz.web.action.ActionBookmark;
import de.bitnoise.sonferenz.web.action.IWebAction;
import de.bitnoise.sonferenz.web.action.WebAction;
import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.pages.auth.LoginPage;
import de.bitnoise.sonferenz.web.pages.proposal.ModelProposalList;
import de.bitnoise.sonferenz.web.pages.proposal.ProposalOverviewPage;
import de.bitnoise.sonferenz.web.pages.proposal.RefToProposal;
import de.bitnoise.sonferenz.web.pages.proposal.ViewProposalPage;
import de.bitnoise.sonferenz.web.pages.voting.UserVoteItem;

public class EditOrViewProposal extends WebAction<IModel<Object>>
// implements AclControlled
    implements ActionBookmark<Object>
{

  public Page doAction(IModel<Object> model)
  {
    Object obj = model.getObject();
    ProposalModel talk = null;
    if (obj instanceof ModelProposalList)
    {
      talk = ((ModelProposalList) obj).talk;
    }
    else if (obj instanceof UserVoteItem)
    {
      talk = ((UserVoteItem) obj).talk;
    }
    else if (obj instanceof ProposalModel)
    {
      talk = ((ProposalModel) obj);
    }
    if (talk != null)
    {
      if (KonferenzSession.noUserLoggedIn())
      {
        return new LoginPage((IWebAction) this, model);
      }
      ProposalOverviewPage userOverviewPage = new ProposalOverviewPage();
      if (KonferenzSession.isUser(talk.getOwner())
          || KonferenzSession.isAdmin())
      {
        userOverviewPage.editTalk(talk);
      }
      else
      {
        userOverviewPage.viewTalk(talk);
      }
      return userOverviewPage;
    }
    return new ProposalOverviewPage();
  }

  public Link<Object> createBookmark(IModel<Object> model, String id)
  {
    Object obj = model.getObject();
    ProposalModel talk = null;
    if (obj instanceof RefToProposal)
    {
      talk = ((RefToProposal) obj).getTalk();
    }
    else if (obj instanceof UserVoteItem)
    {
      talk = ((UserVoteItem) obj).talk;
    }
    if (talk != null)
    {
      PageParameters param = new PageParameters();
      param.add(ViewProposalPage.PARAM_ID, "" + talk.getId());
      return new BookmarkablePageLink(id, ViewProposalPage.class, param);
    }
    else
    {
      return null;
    }
  }

  // public boolean canAccess(IModel model)
  // {
  // Object obj = model.getObject();
  // if (obj instanceof ModelTalkList)
  // {
  // ModelTalkList talk = (ModelTalkList) obj;
  // return KonferenzSession.isUser(talk.talk.getOwner());
  // }
  // return KonferenzSession.hasRight(Right.Talk.Edit);
  // }

}
