package de.bitnoise.sonferenz.web.pages.profile;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import de.bitnoise.sonferenz.model.ActionModel;
import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.model.WhishModel;
import de.bitnoise.sonferenz.service.v2.services.StaticContentService;
import de.bitnoise.sonferenz.service.v2.services.TalkService;
import de.bitnoise.sonferenz.service.v2.services.WhishService;
import de.bitnoise.sonferenz.web.component.TableBuilder;
import de.bitnoise.sonferenz.web.component.TableBuilder.Column;
import de.bitnoise.sonferenz.web.pages.base.AbstractListPanel;
import de.bitnoise.sonferenz.web.pages.profile.MyOverviewPanel.TalkListItem;
import de.bitnoise.sonferenz.web.pages.talks.EditTalkPanel;
import de.bitnoise.sonferenz.web.pages.talks.RefToTalk;
import de.bitnoise.sonferenz.web.pages.talks.action.EditOrViewTalk;
import de.bitnoise.sonferenz.web.pages.whish.ReftoWhish;
import de.bitnoise.sonferenz.web.pages.whish.action.EditOrViewWhish;

public class MyOverviewPanel extends Panel {

  @SpringBean
  StaticContentService text;
  
	public MyOverviewPanel(String id) {
		super(id);
		add(new MyTalksPanel("listMyTalks"));
		add(new MyWhishesPanel("listMyWhishes"));
    add(new Label("headerTalks"  ,text.text("page.profile.talk.header",""))
      .setEscapeModelStrings(false));
    add(new Label("headerWhishes",text.text("page.profile.whish.header",""))
      .setEscapeModelStrings(false));

	}

	public static class WhishListItem implements Serializable, ReftoWhish {
		public String title;
		public WhishModel whish;
		@Override
        public WhishModel getWhish() {
	        return whish;
        }

	}

	public static class MyWhishesPanel extends AbstractListPanel<WhishListItem, WhishModel> {

		@SpringBean
		WhishService whishService;
		
		public MyWhishesPanel(String id) {
			super(id, "myWhishes");
		}

		@Override
		protected WhishListItem transferDbToViewModel(WhishModel dbObject) {
			WhishListItem item = new WhishListItem();
			item.title=dbObject.getTitle();
			item.whish = dbObject;
			return item;
		}

		@Override
		protected Page<WhishModel> getItems(PageRequest request) {
			return whishService.getMyWhishes(request);
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

	public static class TalkListItem implements Serializable, RefToTalk {
		public String title;
		public TalkModel talk;

		@Override
		public TalkModel getTalk() {
			return talk;
		}
	}

	public static class MyTalksPanel extends AbstractListPanel<TalkListItem, TalkModel> {

		@SpringBean
		TalkService talkService;

		public MyTalksPanel(String id) {
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
			Page<TalkModel> list = talkService.getMyTalks(request);
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
