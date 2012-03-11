package de.bitnoise.sonferenz.web.pages.statics;

import org.apache.wicket.markup.html.panel.Panel;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.StaticContentPanel;

@At(url = "/agenda")
public class AgendaPage extends KonferenzPage {
	@Override
	protected Panel getPageContent(String id) {
		return new StaticContentPanel(id, "page.agenda");
	}

}
