package de.bitnoise.sonferenz.web.pages.proposal;

import java.util.ArrayList;

import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.ProposalModel;
import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.app.Right;
import de.bitnoise.sonferenz.web.component.navigation.NavCallbackInterface;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.UnauthorizedPanel;
import de.bitnoise.sonferenz.web.pages.proposal.action.CreateProposal;

@At(url = "/proposals")
public class ProposalOverviewPage extends KonferenzPage
{
  public enum State
  {
    LIST, EDIT, NEW, VIEW
  }

  State state = State.LIST;
  private ProposalModel _talk;

  @SpringBean
  UiFacade facade;
  
  @Override
  protected Panel getPageContent(String id)
  {
    if (!KonferenzSession.hasRight(Right.Talk.List))
    {
      return new UnauthorizedPanel(id);
    }
    switch (state)
    {
    case LIST:
      return new ListProposalPanel(id);
    case EDIT:
      return new EditProposalPanel(id, _talk);
    case VIEW:
      return new ViewProposalPanel(id, _talk);
    case NEW:
      return new EditProposalPanel(id, _talk);
    default:
      return new EmptyPanel(id);
    }
  }

  public void editTalk(ProposalModel talk)
  {
    state = State.EDIT;
    _talk = talk;
  }

  public void viewTalk(ProposalModel talk)
  {
    state = State.VIEW;
    _talk = talk;
  }

  public void createNew()
  {
    state = State.NEW;
    _talk = new ProposalModel();
    _talk.setOwner(facade.getCurrentUser());
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
