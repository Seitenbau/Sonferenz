package de.bitnoise.sonferenz.web.pages.play;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.visural.wicket.aturl.At;
import com.visural.wicket.aturl.URLType;

import de.bitnoise.sonferenz.model.SuggestionModel;
import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.service.v2.services.TalkService;
import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.UnauthorizedPanel;
import de.bitnoise.sonferenz.web.pages.error.UnauthorisedAccess;
import de.bitnoise.sonferenz.web.pages.suggestion.action.EditOrViewWhish;

@At(url = "/view/talk")
public class PlayVideoPage extends KonferenzPage
{
  long talkId;
  
  public PlayVideoPage() {
    
  }
  public PlayVideoPage(PageParameters parameters)
  {
    super(parameters);
    talkId =parameters.getLong("id");
  }
  @Override
  protected Panel getPageContent(String id)
  {
    if(KonferenzSession.noUserLoggedIn()) {
      return new UnauthorizedPanel(id);
    } else {
      return new PlayVideoPanel(id,talkId);
    }
  }
}
