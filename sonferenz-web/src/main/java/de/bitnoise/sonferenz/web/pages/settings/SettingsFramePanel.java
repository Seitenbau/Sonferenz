package de.bitnoise.sonferenz.web.pages.settings;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import de.bitnoise.sonferenz.web.pages.profile.MyTokensPanel;

public class SettingsFramePanel extends Panel {

	public SettingsFramePanel(String id) {
		super(id);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		List<ITab> tabs = new ArrayList<ITab>();
		
		tabs.add(new AbstractTab( Model.of ("usersettings") ) {
			@Override
			public Panel getPanel(String panelId) {
				return new TabUserSetting(panelId);
			}
		});
		tabs.add(new AbstractTab( Model.of ("userInvite") ) {
			@Override
			public Panel getPanel(String panelId) {
				return new MyTokensPanel(panelId);
//				return new TabUserInvite(panelId);
			}
		});

		add(new AjaxTabbedPanel("tabs", tabs));
	}

}
