package de.bitnoise.sonferenz.web.pages.profile;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import de.bitnoise.sonferenz.KonferenzSession;
import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.ActionModel;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.web.component.TableBuilder;
import de.bitnoise.sonferenz.web.component.link.AjaxLink;
import de.bitnoise.sonferenz.web.pages.base.AbstractListPanel;
import de.bitnoise.sonferenz.web.pages.profile.action.TokenCreateUser;
import de.bitnoise.sonferenz.web.pages.statics.ConferencePage;

public class MyInvitesPanel extends AbstractListPanel<TokenListItem, ActionModel>
{
	@SpringBean
	UiFacade facade;

	UserModel user;

	public MyInvitesPanel(String id)
	{
		super(id, "tokenTable");
	}

	@Override
	protected void initColumns(TableBuilder<TokenListItem> builder)
	{
		builder.addColumn("action");
		builder.addColumn("token");
		builder.addColumn("url");
	}

	@Override
	protected Component createAbovePanel(final String id)
	{
		AjaxLink btn = new AjaxLink(id, "profile.user.invites")
		{
			@Override
			protected void onClickLink(AjaxRequestTarget target)
			{
				 setResponsePage(  InviteUserPage.class);
			}
		};
		return btn;
	}

	@Override
	protected TokenListItem transferDbToViewModel(ActionModel dbObject)
	{
		TokenListItem item = new TokenListItem();
		item.action = dbObject.getAction();
		item.token = dbObject.getToken();
		item.url = "/action/" + item.action + "/token/" + item.token;
		return item;
	}

	@Override
	protected Page<ActionModel> getItems(PageRequest request)
	{
		UserModel user = KonferenzSession.get().getCurrentUser();
		return facade.getUserActions(request, user);
	}

}
