package de.bitnoise.sonferenz.web.pages.profile;

import org.apache.wicket.markup.html.panel.Panel;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.app.Right;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.UnauthorizedPanel;

@At(url="/user/invite")
public class InviteUserPage extends KonferenzPage {
	@Override
	protected Panel getPageContent(String id) {
	  if ( KonferenzSession.hasRight(Right.Actions.InviteUser) ) {
		  return new InviteUserPanel(id);
	  } else {
	    return new UnauthorizedPanel(id);
	  }
	}
}
