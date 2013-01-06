package de.bitnoise.sonferenz.web.pages.talks;

import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.service.v2.services.TalkService;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.error.UnauthorisedAccess;
import de.bitnoise.sonferenz.web.pages.talks.action.EditOrViewTalk;

@At(url = "/talk")
public class ViewTalkPage extends KonferenzPage
{
  public static final String PARAM_ID = "id";

  @SpringBean
  private TalkService talkService;

  public ViewTalkPage(PageParameters parameters)
  {
    super();
    long id = parameters.get(PARAM_ID).toLong(-1L);
    if (id == -1L)
    {
      setResponsePage(UnauthorisedAccess.class);
    }
    else
    {
      TalkModel talk = talkService.getTalkById(id);
      Model model = new Model(talk);
      setResponsePage(new EditOrViewTalk().doAction(model));
    }
  }
}
