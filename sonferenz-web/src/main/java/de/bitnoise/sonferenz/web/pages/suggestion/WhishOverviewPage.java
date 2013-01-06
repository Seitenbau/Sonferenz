package de.bitnoise.sonferenz.web.pages.suggestion;

import java.util.ArrayList;

import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.SuggestionModel;
import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.app.Right;
import de.bitnoise.sonferenz.web.component.navigation.NavCallbackInterface;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.UnauthorizedPanel;

@At(url = "/suggestions")
public class WhishOverviewPage extends KonferenzPage
{
  public enum State
  {
    LIST, EDIT, NEW, VIEW
  }

  State state = State.LIST;

  SuggestionModel _whish;

  @SpringBean
  UiFacade facade;

  @Override
  protected Panel getPageContent(String id)
  {
    if (!KonferenzSession.hasRight(Right.Whish.List))
    {
      return new UnauthorizedPanel(id);
    }
    switch (state)
    {
    case LIST:
      return new ListWhishesPanel(id);
    case EDIT:
    case NEW:
      return new EditWhishPanel(id, _whish);
    case VIEW:
      return new ViewWhishPanel(id, _whish);
    default:
      return new EmptyPanel(id);
    }
  }
  @Override
  protected void buildNavigation(ArrayList<NavCallbackInterface> navs)
  {
//    navs.add(new CreateWhish());
  }
  @Override
  public Object getCurrentAction()
  {
    return state;
  }

  public void editTalk(SuggestionModel talk)
  {
    state = State.EDIT;
    _whish = talk;
  }

  public void viewTalk(SuggestionModel talk)
  {
    state = State.VIEW;
    _whish = talk;
  }

  public void createNew()
  {
    state = State.NEW;
    _whish = new SuggestionModel();
    _whish.setOwner(facade.getCurrentUser());
  }
}
