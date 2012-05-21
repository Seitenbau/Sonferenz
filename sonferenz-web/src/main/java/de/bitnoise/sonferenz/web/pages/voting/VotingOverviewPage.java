package de.bitnoise.sonferenz.web.pages.voting;

import org.apache.wicket.markup.html.panel.Panel;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.component.state.OnStateVoting;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.UnauthorizedPanel;

@At(url = "/voting")
public class VotingOverviewPage extends KonferenzPage
{
  public enum State
  {
    LIST
  }

  private State _state = State.LIST;

  @Override
  protected void onInitialize()
  {
    super.onInitialize();
  }

  @Override
  protected Panel getPageContent(String id)
  {
    if (KonferenzSession.noUserLoggedIn() || !new OnStateVoting().canBeDisplayed())
    {
      return new UnauthorizedPanel(id);
    }
    return new ListVotesPanel2(id);
  }

  @Override
  public Object getCurrentAction()
  {
    return _state;
  }
}
