package de.bitnoise.sonferenz.web.pages.admin.actions;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;

import de.bitnoise.sonferenz.web.action.WebAction;
import de.bitnoise.sonferenz.web.pages.admin.model.UserListItem;
import de.bitnoise.sonferenz.web.pages.users.UserOverviewPage;

public class EditUser  extends WebAction<IModel<UserListItem>> 
{

  public Page doAction(IModel<UserListItem> model)
  {
    UserOverviewPage userOverviewPage = new UserOverviewPage();
    userOverviewPage.editUser(model.getObject());
    return userOverviewPage;
  }

}
