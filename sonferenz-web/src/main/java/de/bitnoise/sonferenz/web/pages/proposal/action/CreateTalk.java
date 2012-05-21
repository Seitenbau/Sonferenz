package de.bitnoise.sonferenz.web.pages.proposal.action;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;

import de.bitnoise.sonferenz.web.action.WebMenuAction;
import de.bitnoise.sonferenz.web.pages.proposal.TalksOverviewPage;

public class CreateTalk extends WebMenuAction<IModel<Object>>
{

  public CreateTalk()
  {
    super("Add", TalksOverviewPage.State.NEW);
  }

  public Page doAction(IModel<Object> model)
  {
    TalksOverviewPage page = new TalksOverviewPage();
    page.createNew();
    return page;
  }

//  @Override
//  public boolean isVisible()
//  {
//      return KonferenzSession.get().getCurrentUser() != null ;
//  }
}
