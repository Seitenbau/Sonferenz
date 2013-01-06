package de.bitnoise.sonferenz.web.pages.conference.action;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;

import de.bitnoise.sonferenz.web.action.WebMenuAction;
import de.bitnoise.sonferenz.web.pages.conference.ConferenceOverviewPage;

public class AddConference extends WebMenuAction<IModel<Object>>
{
  public AddConference()
  {
    super("Add", ConferenceOverviewPage.State.NEW);
  }

  public Page doAction(IModel<Object> model)
  {
    ConferenceOverviewPage target = new ConferenceOverviewPage();
    target.createNew();
    return target;
  }

}
