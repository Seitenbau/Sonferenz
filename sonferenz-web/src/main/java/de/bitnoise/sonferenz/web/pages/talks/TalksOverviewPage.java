package de.bitnoise.sonferenz.web.pages.talks;

import java.util.ArrayList;

import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.ProposalModel;
import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.app.Right;
import de.bitnoise.sonferenz.web.component.navigation.NavCallbackInterface;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.UnauthorizedPanel;

@At(url = "/talks")
public class TalksOverviewPage extends KonferenzPage
{
  public enum State
  {
    LIST, EDIT, NEW, VIEW
  }

  State state = State.LIST;
  TalkModel _talk;

  @Override
  protected Panel getPageContent(String id)
  {
    if (KonferenzSession.noUserLoggedIn())
    {
      return new UnauthorizedPanel(id);
    }
    switch (state)
    {
    case LIST:
      return new ListTalksPanel(id);
    case EDIT:
      return new EditTalkPanel(id, _talk);
    case VIEW:
      return new ViewTalkPanel(id, _talk);
    case NEW:
      return new EditTalkPanel(id, _talk);
    default:
      return new EmptyPanel(id);
    }
  }

  public void editTalk(TalkModel talk)
  {
    state = State.EDIT;
    _talk = talk;
  }

  public void viewTalk(TalkModel talk)
  {
    state = State.VIEW;
    _talk = talk;
  }

  public void createNew()
  {
    state = State.NEW;
    _talk = new TalkModel();
    _talk.setOwner(KonferenzSession.get().getCurrentUser());
  }

  @Override
  protected void buildNavigation(ArrayList<NavCallbackInterface> navs)
  {
//    navs.add(new CreateTalk());
  }

  @Override
  public Object getCurrentAction()
  {
    return state;
  }
}
