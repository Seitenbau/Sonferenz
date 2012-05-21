package de.bitnoise.sonferenz.web.pages.suggestion.action;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;

import de.bitnoise.sonferenz.web.action.WebMenuAction;
import de.bitnoise.sonferenz.web.pages.suggestion.WhishOverviewPage;

public class CreateWhish extends WebMenuAction<IModel<Object>>
{

  public CreateWhish()
  {
    super("Add", WhishOverviewPage.State.NEW);
  }

  public Page doAction(IModel<Object> model)
  {
    WhishOverviewPage page = new WhishOverviewPage();
    page.createNew();
    return page;
  }

}
