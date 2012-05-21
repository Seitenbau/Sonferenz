package de.bitnoise.sonferenz.web.pages.proposal;

import org.apache.wicket.PageParameters;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.proposal.action.EditOrViewTalk;

@At(url = "/paper")
public class ViewTalkPage extends KonferenzPage
{
  public static final String PARAM_ID = "id";
  
  @SpringBean
  private UiFacade facade;

  public ViewTalkPage(PageParameters parameters)
  {
    super();
    int id = parameters.getInt(PARAM_ID);
    TalkModel talk = facade.getTalkById(id);
    Model model = new Model(talk);
    setResponsePage(new EditOrViewTalk().doAction(model));
  }
}
