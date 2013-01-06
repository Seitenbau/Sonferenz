package de.bitnoise.sonferenz.web.pages.conference.action;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;

import de.bitnoise.sonferenz.web.action.WebAction;
import de.bitnoise.sonferenz.web.pages.conference.ConferenceOverviewPage;
import de.bitnoise.sonferenz.web.pages.conference.table.ConferenceListItem;

public class EditConference extends WebAction<IModel<ConferenceListItem>>
{

  public Page doAction(IModel<ConferenceListItem> model)
  {
    ConferenceOverviewPage page = new ConferenceOverviewPage();
    page.edit(model.getObject());
    return page;
  }

}
