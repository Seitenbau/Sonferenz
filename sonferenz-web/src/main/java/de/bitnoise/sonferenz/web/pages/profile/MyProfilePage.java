package de.bitnoise.sonferenz.web.pages.profile;

import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.KonferenzSession;
import de.bitnoise.sonferenz.Right;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.web.component.panels.MultiPanel;
import de.bitnoise.sonferenz.web.component.tabs.TabPanel;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.UnauthorizedPanel;
import de.bitnoise.sonferenz.web.pages.users.ListUserPanel;

@At(url = "/profile")
public class MyProfilePage extends KonferenzPage
{
  @Override
  protected Panel getPageContent(String id)
  {
    final UserModel user = KonferenzSession.get().getCurrentUser();
    if (user != null)
    {
    	return new TabPanel(id) {
			@Override
			protected void createTabs(List<ITab> tabs) {
//				tabs.add(new AbstractTab(txt("profile.tab.settings")) {
//					@Override
//					public Panel getPanel(String panelId) {
//						return new MySettingsPanel(panelId);
//					}
//				});
				if ( KonferenzSession.hasRight(Right.Actions.InviteUser) ) {
					tabs.add(new AbstractTab(txt("profile.tab.invites")) {
						@Override
						public Panel getPanel(String panelId) {
							return new MyInvitesPanel(panelId);
						}
					});
				}
				tabs.add(new AbstractTab(txt("profile.tab.profile")) {
				  @Override
				  public Panel getPanel(String panelId) {
				    return new MyProfilePanel(panelId);
				  }
				});
			}};
    }
    else
    {
      return new UnauthorizedPanel(id);
    }
  }
}
