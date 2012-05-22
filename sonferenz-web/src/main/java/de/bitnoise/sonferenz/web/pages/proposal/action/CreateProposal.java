package de.bitnoise.sonferenz.web.pages.proposal.action;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;

import de.bitnoise.sonferenz.web.action.WebMenuAction;
import de.bitnoise.sonferenz.web.pages.proposal.ProposalOverviewPage;

public class CreateProposal extends WebMenuAction<IModel<Object>>
{

  public CreateProposal()
  {
    super("Add", ProposalOverviewPage.State.NEW);
  }

  public Page doAction(IModel<Object> model)
  {
    ProposalOverviewPage page = new ProposalOverviewPage();
    page.createNew();
    return page;
  }

//  @Override
//  public boolean isVisible()
//  {
//      return KonferenzSession.get().getCurrentUser() != null ;
//  }
}
