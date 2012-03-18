package de.bitnoise.sonferenz.web.component.tabs;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;

public abstract class TabPanel extends Panel {

	public TabPanel(String id) {
		super(id);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		List<ITab> tabs = new ArrayList<ITab>();
		
		createTabs(tabs);

		add(new AjaxTabbedPanel("tabs", tabs));
	}

	protected abstract void createTabs(List<ITab> tabs) ;

}
