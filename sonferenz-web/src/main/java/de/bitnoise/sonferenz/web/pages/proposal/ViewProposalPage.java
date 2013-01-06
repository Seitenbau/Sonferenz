package de.bitnoise.sonferenz.web.pages.proposal;

import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.ProposalModel;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.proposal.action.EditOrViewProposal;

@At(url = "/proposal")
public class ViewProposalPage extends KonferenzPage
{
  public static final String PARAM_ID = "id";
  
  @SpringBean
  private UiFacade facade;

  public ViewProposalPage(PageParameters parameters)
  {
    super();
    int id = parameters.get(PARAM_ID).toInt();
    ProposalModel talk = facade.getProposalById(id);
    Model model = new Model(talk);
    setResponsePage(new EditOrViewProposal().doAction(model));
  }
}
