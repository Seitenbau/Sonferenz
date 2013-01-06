package de.bitnoise.sonferenz.web.pages.play;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.UnauthorizedPanel;

@At(url = "/view/talk")
public class PlayVideoPage extends KonferenzPage
{
  long talkId;
  
  public PlayVideoPage() {
    
  }
  public PlayVideoPage(PageParameters parameters)
  {
    super(parameters);
    talkId =parameters.get("id").toLong();
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
