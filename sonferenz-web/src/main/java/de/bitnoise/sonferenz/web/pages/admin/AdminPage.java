package de.bitnoise.sonferenz.web.pages.admin;

import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.KonferenzSession;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.web.component.tabs.TabPanel;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.UnauthorizedPanel;
import de.bitnoise.sonferenz.web.pages.conference.ListConferencesPanel;
import de.bitnoise.sonferenz.web.pages.config.ListConfigPanel;
import de.bitnoise.sonferenz.web.pages.config.ListRolesPanel;
import de.bitnoise.sonferenz.web.pages.config.ListTextePanel;
import de.bitnoise.sonferenz.web.pages.users.ListUserPanel;

@At(url = "/admin")
public class AdminPage extends KonferenzPage {
	@Override
	protected Panel getPageContent(String id)
	{
		final UserModel user = KonferenzSession.get().getCurrentUser();
		if (user != null)
		{
			return new TabPanel(id) {
				@Override
				protected void createTabs(List<ITab> tabs) {
					tabs.add(new AbstractTab(txt("admin.tab.users")) {
						@Override
						public Panel getPanel(String panelId) {
							return new ListUserPanel(panelId);
						}
					});
					tabs.add(new AbstractTab(txt("admin.tab.roles")) {
						@Override
						public Panel getPanel(String panelId) {
							return new ListRolesPanel(panelId);
						}
					});
					tabs.add(new AbstractTab(txt("admin.tab.conference")) {
						@Override
						public Panel getPanel(String panelId) {
							return new ListConferencesPanel(panelId);
						}
					});
					tabs.add(new AbstractTab(txt("admin.tab.config")) {
						@Override
						public Panel getPanel(String panelId) {
							return new ListConfigPanel(panelId);
						}
					});
					tabs.add(new AbstractTab(txt("admin.tab.i18n")) {
						@Override
						public Panel getPanel(String panelId) {
							return new ListTextePanel(panelId);
						}
					});
				}
			};
		}
		else
		{
			return new UnauthorizedPanel(id);
		}
	}
}
