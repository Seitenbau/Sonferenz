package de.bitnoise.sonferenz.web.component.lvl1;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import de.bitnoise.sonferenz.KonferenzSession;
import de.bitnoise.sonferenz.service.v2.services.impl.ConferenceFacadeImplTest;
import de.bitnoise.sonferenz.web.ConfigMainNavigation;
import de.bitnoise.sonferenz.web.component.navigation.NavCallbackInterface;
import de.bitnoise.sonferenz.web.component.navigation.NavPanel;
import de.bitnoise.sonferenz.web.pages.auth.LoginPanel;
import de.bitnoise.sonferenz.web.pages.statics.AgendaPage;
import de.bitnoise.sonferenz.web.pages.statics.ConferencePage;
import de.bitnoise.sonferenz.web.pages.statics.ContactPage;
import de.bitnoise.sonferenz.web.pages.statics.RegisterPage;
import de.bitnoise.sonferenz.web.pages.statics.ReviewPage;
import de.bitnoise.sonferenz.web.pages.talks.ListTalksPanel;
import de.bitnoise.sonferenz.web.pages.talks.TalksOverviewPage;
import de.bitnoise.sonferenz.web.pages.talks.ViewTalkPage;
import de.bitnoise.sonferenz.web.pages.whish.ViewWhishPage;
import de.bitnoise.sonferenz.web.pages.whish.WhishOverviewPage;

public class FirstLevelBar extends Panel {

	public FirstLevelBar(String id) {
		super(id);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		RepeatingView items = new RepeatingView("repeater");
		items.add(new MenuButton("conference", items.newChildId(), ConferencePage.class));
		if (!KonferenzSession.noUserLoggedIn()) {
			items.add(new MenuButton("whishes", items.newChildId(), WhishOverviewPage.class));
			items.add(new MenuButton("talks", items.newChildId(), TalksOverviewPage.class));
			/* 
			items.add(new MenuButton("archive", items.newChildId(), ReviewPage.class));
			items.add(new MenuButton("program", items.newChildId(), ConferencePage.class));
			items.add(new MenuButton("agenda", items.newChildId(), AgendaPage.class));
			*/
		}
		items.add(new MenuButton("register", items.newChildId(), RegisterPage.class));
		items.add(new MenuButton("contact", items.newChildId(), ContactPage.class));
		add(items);
	}
}
