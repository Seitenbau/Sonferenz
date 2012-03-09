package de.bitnoise.sonferenz.web.pages.users;

import java.util.ArrayList;

import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.KonferenzSession;
import de.bitnoise.sonferenz.Right;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.web.component.navigation.NavCallbackInterface;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.UnauthorizedPanel;
import de.bitnoise.sonferenz.web.pages.users.action.CreateNewUser;
import de.bitnoise.sonferenz.web.pages.users.table.UserListItem;

@At(url = "/users")
public class UserOverviewPage extends KonferenzPage
{
  IModel<Integer> _clicked = new Model<Integer>();
  private UserModel _user;

  public enum State
  {
    LIST, EDIT, NEW
  }

  State state = State.LIST;


  @Override
  protected Panel getPageContent(String id)
  {
    if (!KonferenzSession.hasRight(Right.User.List))
    {
      return new UnauthorizedPanel(id);
    }
    switch (state)
    {
    case LIST:
      return new ListUserPanel(id);
    case EDIT:
      return new EditUserPanel(id ,_user);
    case NEW:
      return new EditLocalUserPanel(id);
    default:
      return new EmptyPanel(id);
    }
  }

  public void createNewUser()
  {
    state = State.NEW;
  }

  @Override
  protected void buildNavigation(ArrayList<NavCallbackInterface> navs)
  {
//    navs.add(new CreateNewUser());
  }

  public void editUser(UserListItem object)
  {
    state = State.EDIT;
    _user = object.user;
  }
}
