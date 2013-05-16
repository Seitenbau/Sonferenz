package de.bitnoise.sonferenz.web.pages.admin;

import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.app.Right;
import de.bitnoise.sonferenz.web.component.tabs.TabPanel;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.UnauthorizedPanel;
import de.bitnoise.sonferenz.web.pages.admin.tabs.ActionsPanel;
import de.bitnoise.sonferenz.web.pages.admin.tabs.AppStatePanel;
import de.bitnoise.sonferenz.web.pages.admin.tabs.ListSpeakerPanel;
import de.bitnoise.sonferenz.web.pages.admin.tabs.ListUserPanel;
import de.bitnoise.sonferenz.web.pages.admin.tabs.LogOutputPanel;
import de.bitnoise.sonferenz.web.pages.admin.tabs.MailTab;
import de.bitnoise.sonferenz.web.pages.admin.tabs.VotingResultPanel;
import de.bitnoise.sonferenz.web.pages.conference.ListConferencesPanel;
import de.bitnoise.sonferenz.web.pages.config.ListConfigPanel;
import de.bitnoise.sonferenz.web.pages.config.ListRolesPanel;
import de.bitnoise.sonferenz.web.pages.config.ListTextePanel;
import de.bitnoise.sonferenz.web.pages.talks.ListTalksPanel;

@At(url = "/admin")
public class AdminPage extends KonferenzPage {
	@Override
	protected Panel getPageContent(String id)
	{
		if (KonferenzSession.get().hasRight(Right.Admin.Configure))
		{
			return new TabPanel(id) {
				@Override
				protected void createTabs(List<ITab> tabs) {
					tabs.add(new AbstractTab(txt("admin.tab.speakers")) {
						@Override
						public Panel getPanel(String panelId) {
							return new ListSpeakerPanel(panelId);
						}
					});
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
					tabs.add(new AbstractTab(txt("admin.tab.logs")) {
					  @Override
					  public Panel getPanel(String panelId) {
					    return new LogOutputPanel(panelId);
					  }
					});
					tabs.add(new AbstractTab(txt("admin.tab.voteresult")) {
					  @Override
					  public Panel getPanel(String panelId) {
					    return new VotingResultPanel(panelId);
					  }
					});
					tabs.add(new AbstractTab(txt("admin.tab.nextaction")) {
					  @Override
					  public Panel getPanel(String panelId) {
					    return new ActionsPanel(panelId);
					  }
					});
					tabs.add(new AbstractTab(txt("admin.tab.mailings")) {
					  @Override
					  public Panel getPanel(String panelId) {
					    return new MailTab(panelId);
					  }
					});
					tabs.add(new AbstractTab(txt("admin.tab.appstate")) {
						@Override
						public Panel getPanel(String panelId) {
							return new AppStatePanel(panelId);
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
