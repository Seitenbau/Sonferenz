package de.bitnoise.sonferenz.web.pages.statics;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.panel.Panel;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.StaticContentPanel;

@At(url = "/user/info")
public class InfoPage extends KonferenzPage {
	@Override
	protected Panel getPageContent(String id) {
		return new StaticContentPanel(id, "page.info");
	}

	public static PageParameters createParameters(ConferenceModel iModel) {
		PageParameters pp = new PageParameters();
		pp.add("info", String.valueOf(iModel.getId()));
		return pp;
	}

}
