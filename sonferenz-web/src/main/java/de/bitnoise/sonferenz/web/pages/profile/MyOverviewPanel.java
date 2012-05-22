package de.bitnoise.sonferenz.web.pages.profile;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import de.bitnoise.sonferenz.model.ActionModel;
import de.bitnoise.sonferenz.model.ProposalModel;
import de.bitnoise.sonferenz.model.SuggestionModel;
import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.service.v2.services.StaticContentService;
import de.bitnoise.sonferenz.service.v2.services.ProposalService;
import de.bitnoise.sonferenz.service.v2.services.SuggestionService;
import de.bitnoise.sonferenz.service.v2.services.TalkService;
import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.component.TableBuilder;
import de.bitnoise.sonferenz.web.component.TableBuilder.Column;
import de.bitnoise.sonferenz.web.pages.base.AbstractListPanel;
import de.bitnoise.sonferenz.web.pages.profile.MyOverviewPanel.TalkListItem;
import de.bitnoise.sonferenz.web.pages.proposal.EditProposalPanel;
import de.bitnoise.sonferenz.web.pages.proposal.RefToProposal;
import de.bitnoise.sonferenz.web.pages.proposal.action.EditOrViewProposal;
import de.bitnoise.sonferenz.web.pages.suggestion.ReftoWhish;
import de.bitnoise.sonferenz.web.pages.suggestion.action.EditOrViewWhish;
import de.bitnoise.sonferenz.web.pages.talks.RefToTalk;
import de.bitnoise.sonferenz.web.pages.talks.action.EditOrViewTalk;

public class MyOverviewPanel extends Panel {

  @SpringBean
  StaticContentService text;
  
	public MyOverviewPanel(String id) {
		super(id);
		add(new MyTalkPanel("listMyTalks"));
		add(new MyProposalPanel("listMyProposals"));
		add(new MyWhishesPanel("listMyWhishes"));
    add(new Label("headerTalks"  ,text.text("page.profile.talk.header",""))
      .setEscapeModelStrings(false));
    add(new Label("headerWhishes",text.text("page.profile.whish.header",""))
      .setEscapeModelStrings(false));

	}

	public static class WhishListItem implements Serializable, ReftoWhish {
		public String title;
		public SuggestionModel whish;
		@Override
        public SuggestionModel getWhish() {
	        return whish;
        }

	}

	public static class MyWhishesPanel extends AbstractListPanel<WhishListItem, SuggestionModel> {

		@SpringBean
		SuggestionService whishService;
		
		public MyWhishesPanel(String id) {
			super(id, "myWhishes");
		}

		@Override
		protected WhishListItem transferDbToViewModel(SuggestionModel dbObject) {
			WhishListItem item = new WhishListItem();
			item.title=dbObject.getTitle();
			item.whish = dbObject;
			return item;
		}

		@Override
		protected Page<SuggestionModel> getItems(PageRequest request) {
			return whishService.getMySuggestions(request);
		}

		@Override
		protected void initColumns(TableBuilder<WhishListItem> builder) {
			Column c = builder.addColumn("title");
			c.setTitle("Titel");
			c.sortable();
			c.setModelProperty("title");
			c.action(new EditOrViewWhish());
		}

	}

	public static class ProposalListItem implements Serializable, RefToProposal {
		public String title;
		public ProposalModel talk;

		@Override
		public ProposalModel getTalk() {
			return talk;
		}
	}
	public static class TalkListItem implements Serializable, RefToTalk {
	  public String title;
	  public TalkModel talk;
	  
	  @Override
	  public TalkModel getTalk() {
	    return talk;
	  }
	}

	public static class MyProposalPanel extends AbstractListPanel<ProposalListItem, ProposalModel> {

		@SpringBean
		ProposalService proposalService;

		public MyProposalPanel(String id) {
			super(id, "mySuggestions");
		}

		@Override
		protected ProposalListItem transferDbToViewModel(ProposalModel dbObject) {
		  ProposalListItem item = new ProposalListItem();
			item.title = dbObject.getTitle();
			item.talk = dbObject;
			return item;
		}

		@Override
		protected Page<ProposalModel> getItems(PageRequest request) {
			Page<ProposalModel> list = proposalService.getMyProposals(request);
			return list;
		}

		@Override
		protected void initColumns(TableBuilder<ProposalListItem> builder) {
			Column c = builder.addColumn("title");
			c.setTitle("Titel");
			c.sortable();
			c.setModelProperty("title");
			c.action(new EditOrViewProposal());

		}

	}
	public static class MyTalkPanel extends AbstractListPanel<TalkListItem, TalkModel> {
	  
	  @SpringBean
	  TalkService talkService;
	  
	  public MyTalkPanel(String id) {
	    super(id, "myTalks");
	  }
	  
	  @Override
	  protected TalkListItem transferDbToViewModel(TalkModel dbObject) {
	    TalkListItem item = new TalkListItem();
	    item.title = dbObject.getTitle();
	    item.talk = dbObject;
	    return item;
	  }
	  
	  @Override
	  protected Page<TalkModel> getItems(PageRequest request) {
	    Page<TalkModel> list = talkService.getMyTalks(KonferenzSession.context(),request);
	    return list;
	  }
	  
	  @Override
	  protected void initColumns(TableBuilder<TalkListItem> builder) {
	    Column c = builder.addColumn("title");
	    c.setTitle("Titel");
	    c.sortable();
	    c.setModelProperty("title");
	    c.action(new EditOrViewTalk());
	    
	  }
	  
	}

}
