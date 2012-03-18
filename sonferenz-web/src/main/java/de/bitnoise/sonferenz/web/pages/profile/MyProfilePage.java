package de.bitnoise.sonferenz.web.pages.profile;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.KonferenzSession;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.web.component.panels.MultiPanel;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.UnauthorizedPanel;

@At(url = "/profile")
public class MyProfilePage extends KonferenzPage
{
  @Override
  protected Panel getPageContent(String id)
  {
    final UserModel user = KonferenzSession.get().getCurrentUser();
    if (user != null)
    {
      return new MultiPanel(id)
      {
        @Override
        protected void onInitPanels(RepeatingView view)
        {
          view.add( new MyProfilePanel(view.newChildId(), user) );
//          view.add( new MyTokensPanel(view.newChildId()) );
        }
      };  
    }
    else
    {
      return new UnauthorizedPanel(id);
    }
  }
}
