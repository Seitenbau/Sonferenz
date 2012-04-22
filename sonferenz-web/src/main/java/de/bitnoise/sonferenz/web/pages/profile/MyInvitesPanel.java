package de.bitnoise.sonferenz.web.pages.profile;

import javax.persistence.Column;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import ch.qos.logback.core.rolling.helper.PeriodicityType;

import de.bitnoise.sonferenz.KonferenzSession;
import de.bitnoise.sonferenz.Right;
import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.ActionModel;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.service.v2.services.StaticContentService;
import de.bitnoise.sonferenz.web.component.TableBuilder;
import de.bitnoise.sonferenz.web.component.link.AjaxLink;
import de.bitnoise.sonferenz.web.pages.base.AbstractListPanel;
import de.bitnoise.sonferenz.web.pages.statics.ConferencePage;

public class MyInvitesPanel extends AbstractListPanel<TokenListItem, ActionModel>
{
	@SpringBean
	UiFacade facade;

	UserModel user;

	@SpringBean
  StaticContentService text;

	public MyInvitesPanel(String id)
	{
		super(id, "tokenTable");
		setHideWhenEmpty(true);
	}

    @Override
	protected void initColumns(TableBuilder<TokenListItem> builder)
	{
		builder.addColumn("state");
		builder.addColumn("title");
		if( KonferenzSession.hasRight(Right.Actions.ManageInviteUser) ) {
		  builder.addColumn("url");
		  builder.addColumn("username");
		}
		builder.addColumn("expires");
	}

	@Override
	protected Component createAbovePanel(final String id)
	{
	  return new InvitesTop(id);
	}
	
	class InvitesTop extends Panel {
    public InvitesTop(String id)
    {
      super(id);
    }
    @Override
    protected void onInitialize()
    {
      super.onInitialize();
      AjaxLink btn = new AjaxLink("button", "profile.user.invites")
      {
        @Override
        protected void onClickLink(AjaxRequestTarget target)
        {
           setResponsePage(InviteUserPage.class);
        }
      };
      add ( btn);
      add(new Label("headerText",text.text("page.profile.invite.header",""))
      .setEscapeModelStrings(false));
    }
	  
	}

	@Override
	protected TokenListItem transferDbToViewModel(ActionModel dbObject)
	{
		TokenListItem item = new TokenListItem();
		String action = dbObject.getAction();
		String token = dbObject.getToken();
		item.title = dbObject.getTitle();
		item.username = dbObject.getCreator().getName();
		DateTime expiry = new DateTime(dbObject.getExpiry());
		DateTime now = DateTime.now();
		Duration d = new Duration(now, expiry);
		StringBuffer sb = new StringBuffer();
		if(d.getMillis()<=100) {
		  sb.append("expired");
		} else {
  		if( d.toStandardDays().getDays() > 1) {
  		  sb.append( d.toStandardDays().getDays() );
  		  sb.append("d");
  		} else if(d.toStandardHours().getHours() > 1){
  		  sb.append(d.toStandardHours().getHours() );
  		  sb.append("h");
  		} else {
  		  sb.append(d.toStandardMinutes() );
  		  sb.append("m");
  		}
		}
		item.expires=sb.toString();
		
		Integer used = dbObject.getUsed();
		if(used!=null && used > 0) {
			item.state = "used";
		} else {
			item.state = "pending";
		}
		item.url = "/action/" +  action + "/token/" +  token;
		return item;
	}

	@Override
	protected Page<ActionModel> getItems(PageRequest request)
	{
		UserModel user = KonferenzSession.get().getCurrentUser();
		if( KonferenzSession.hasRight(Right.Actions.ManageInviteUser) ) {
			return facade.getAllUserActions(request,user);
		} else {
			return facade.getUserActions(request, user);
		}
	}

}
