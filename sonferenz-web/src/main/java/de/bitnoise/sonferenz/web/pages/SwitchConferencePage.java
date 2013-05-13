package de.bitnoise.sonferenz.web.pages;

import javax.inject.Inject;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.StaticContentPanel;
import de.bitnoise.sonferenz.web.pages.statics.ConferencePage;

@At(url = "/conference")
public class SwitchConferencePage extends ConferencePage {

	@Inject
	UiFacade facade;
	public SwitchConferencePage(PageParameters params) {
		System.out.println(params);
		StringValue param = params.get("conference");
		if( param.isEmpty() ) 
		{
			setResponsePage(ConferencePage.class);
			return;
		}
		int id = param.toInt(-1);
		if( id == -1 ) {
			setResponsePage(ConferencePage.class);
			return;
		}
		ConferenceModel conference = facade.getConference(id);
		if( conference == null ) {
			setResponsePage(ConferencePage.class);
			return;
		}
		KonferenzSession.get().setCurrentConference(conference);
		setResponsePage(ConferencePage.class);
	}

	public static PageParameters createParameters(ConferenceModel iModel) {
		PageParameters pp = new PageParameters();
		pp.add("conference", String.valueOf(iModel.getId()));
		return pp;
	}

}
