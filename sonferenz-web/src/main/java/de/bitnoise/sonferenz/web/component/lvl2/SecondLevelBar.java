package de.bitnoise.sonferenz.web.component.lvl2;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import de.bitnoise.sonferenz.web.ConfigMainNavigation;
import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.component.navigation.NavCallbackInterface;
import de.bitnoise.sonferenz.web.component.navigation.NavPanel;
import de.bitnoise.sonferenz.web.pages.auth.LoginPanel;

public class SecondLevelBar extends Panel {

	public SecondLevelBar(String id) {
		super(id);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		IModel<NavCallbackInterface> callbackModel = new LoadableDetachableModel<NavCallbackInterface>()
		{
			@Override
			protected NavCallbackInterface load()
			{
				return ConfigMainNavigation.getMainNaviagtion();
			}
		};
		
		add(new LoginPanel("loginPanel"));
		add(new NavPanel("menuPanel", callbackModel));
	}
}
