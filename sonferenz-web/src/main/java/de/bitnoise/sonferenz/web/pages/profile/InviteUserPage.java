package de.bitnoise.sonferenz.web.pages.profile;

import org.apache.wicket.markup.html.panel.Panel;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.web.pages.KonferenzPage;

@At(url="/user/invite")
public class InviteUserPage extends KonferenzPage {
	@Override
	protected Panel getPageContent(String id) {
		return new InviteUserPanel(id);
	}
}
