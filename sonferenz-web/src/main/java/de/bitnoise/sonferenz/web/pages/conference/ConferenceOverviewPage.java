package de.bitnoise.sonferenz.web.pages.conference;

import java.util.ArrayList;

import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.app.Right;
import de.bitnoise.sonferenz.web.component.navigation.NavCallbackInterface;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.UnauthorizedPanel;
import de.bitnoise.sonferenz.web.pages.conference.action.AddConference;
import de.bitnoise.sonferenz.web.pages.conference.table.ConferenceListItem;

@At(url = "/conferences")
public class ConferenceOverviewPage extends KonferenzPage
{
  public enum State
  {
    LIST, EDIT, NEW
  }

  State _state = State.LIST;

  private ConferenceListItem _conference;

  @Override
  protected Panel getPageContent(String id)
  {
    if (!KonferenzSession.hasRight(Right.Conference.List))
    {
      return new UnauthorizedPanel(id);
    }
    switch (_state)
    {
    case LIST:
      return new ListConferencesPanel(id);
    case EDIT:
      return new EditConferenceWizard(id, _conference.conference);
    case NEW:
      ConferenceModel conference=new ConferenceModel();
      return new EditConferenceWizard(id, conference);
    default:
      return new EmptyPanel(id);
    }
  }

  @Override
  public Object getCurrentAction()
  {
    return _state;
  }

  @Override
  protected void buildNavigation(ArrayList<NavCallbackInterface> elements)
  {
//    elements.add(new AddConference());
  }

  public void edit(ConferenceListItem object)
  {
    _state = State.EDIT;
    _conference = object;
  }

  public void createNew()
  {
    _state = State.NEW;
  }
}
