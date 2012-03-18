package de.bitnoise.sonferenz.web.pages.settings;

import org.apache.wicket.markup.html.panel.Panel;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.KonferenzSession;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.UnauthorizedPanel;

@At(url = "/usersetting")
public class SettingsPage extends KonferenzPage {

	@Override
	protected Panel getPageContent(String id)
	{
		final UserModel user = KonferenzSession.get().getCurrentUser();
		if (user != null)
		{
			return new SettingsFramePanel(id);
		}
		else
		{
			return new UnauthorizedPanel(id);
		}
	}

}
