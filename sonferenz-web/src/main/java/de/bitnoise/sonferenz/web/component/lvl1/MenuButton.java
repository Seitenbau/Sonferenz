package de.bitnoise.sonferenz.web.component.lvl1;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;

import de.bitnoise.sonferenz.web.component.navigation.VisibleChoice;
import de.bitnoise.sonferenz.web.component.state.OnStateVoting;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.utils.WicketTools;

public class MenuButton extends Panel {

	private VisibleChoice[] vis;

	public MenuButton(String linkTarget, String id, Class target , VisibleChoice ... visibleChoice) {
		super(id);
		vis = visibleChoice;
		BookmarkablePageLink<String> link = new BookmarkablePageLink<String>("link",target);
		add(link);
		Label text = new Label("linkText",KonferenzPage.txt("menu.1lvl." + linkTarget));
		WicketTools.addCssClass(link, linkTarget);
		link.add(text);
	}
	
	@Override
	protected void onConfigure() {
		boolean visible=true;
		for(VisibleChoice v : vis) 
		{
			if(!v.canBeDisplayed()) 
			{
				visible = false;
			}
		}
		setVisible(visible);
	}

}
