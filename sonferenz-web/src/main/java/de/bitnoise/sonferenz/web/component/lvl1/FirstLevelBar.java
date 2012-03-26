package de.bitnoise.sonferenz.web.component.lvl1;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;

import de.bitnoise.sonferenz.KonferenzSession;
import de.bitnoise.sonferenz.web.pages.statics.ConferencePage;
import de.bitnoise.sonferenz.web.pages.statics.ContactPage;
import de.bitnoise.sonferenz.web.pages.statics.RegisterPage;
import de.bitnoise.sonferenz.web.pages.talks.TalksOverviewPage;
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