package de.bitnoise.sonferenz.web.pages.reset;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.web.action.IWebAction;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.statics.ConferencePage;

@At(url = "/forgotten/username")
public class QueryUser extends KonferenzPage
{
	@Override
	protected Panel getPageContent(String id) {
		return new QueryUserPanel(id);
	}
}
