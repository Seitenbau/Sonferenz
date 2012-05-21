package de.bitnoise.sonferenz.web.pages.proposal;

import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;


import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.service.v2.services.StaticContentService;
import de.bitnoise.sonferenz.web.component.SortableServiceDataProvider;
import de.bitnoise.sonferenz.web.component.TableBuilder;
import de.bitnoise.sonferenz.web.component.link.AjaxLink;
import de.bitnoise.sonferenz.web.pages.admin.actions.CreateNewUser;
import de.bitnoise.sonferenz.web.pages.proposal.action.CreateTalk;
import de.bitnoise.sonferenz.web.pages.proposal.action.EditOrViewTalk;
import de.bitnoise.sonferenz.web.pages.users.UserOverviewPage;
import de.bitnoise.sonferenz.web.toolbar.AddToolbarWithButton;

public class ListTalksPanel extends Panel
{
  

  @SpringBean
  StaticContentService content2;
  
  @SpringBean 
  UiFacade facade;
  
  public ListTalksPanel(String id)
  {
    super(id);
    InjectorHolder.getInjector().inject(this);
  }

  @Override
  protected void onInitialize()
  {
    super.onInitialize();
    final Set<ModelTalkList> selected = new HashSet<ModelTalkList>();
    TableBuilder<ModelTalkList> builder = new TableBuilder<ModelTalkList>(
        "talks")
    {
      {
        addColumn(new Column()
        {
          {
            setTitle("Titel");
            setModelProperty("title");
            sortable();
            action(new EditOrViewTalk());
          }
        });
//        addColumn(new Column()
//        {
//          {
//            setTitle("Description");
//            setEscaping(false);
//            setModelProperty("description");
//          }
//        });
        addColumn(new Column()
        {
          {
            setTitle("Author");
            setModelProperty("author");
            sortable();
          }
        });
        // addColumn(new Column() {
        // {
        // setTitle("Owner");
        // setModelProperty("owner");
        // sortable();
        // }
        // });
      }
    };

    ISortableDataProvider<ModelTalkList> provider = new SortableServiceDataProvider<TalkModel, ModelTalkList>( )
    {
      @Override
      protected ModelTalkList transferType(TalkModel dbObject)
      {
        ModelTalkList item = new ModelTalkList();
        item.author = dbObject.getAuthor();
        item.title = dbObject.getTitle();
        item.owner = dbObject.getOwner().toString();
        item.description = createShortDescription(dbObject);
        item.talk = dbObject;
        return item;
      }

      @Override
      protected Page<TalkModel> getAllItems(PageRequest request)
      {
        return facade.getTalks(request);
      }

      @Override
      public int size()
      {
        return facade.getAllTalksCount();
      }

      @Override
      protected Sort createDefaultSorting()
      {
        return new Sort(new Order(Direction.ASC, "title"));
      }

    };
    String text=content2.text("page.talkHeader","");
    add(new Label("headerText",text).setEscapeModelStrings(false));
    DefaultDataTable<ModelTalkList> table = new DefaultDataTable<ModelTalkList>(
        "talkTable", builder.getColumns(), provider, 100);
    
    add(new AjaxLink("above","table.talks.create") {
		@Override
		protected void onClickLink(AjaxRequestTarget target) {
			TalksOverviewPage page = new TalksOverviewPage();
		    page.createNew();
			setResponsePage(page );
		}
	});
    
//    AddToolbarWithButton toolbar = new AddToolbarWithButton("+ create Talk",
//            table, new CreateTalk());
//        table.addBottomToolbar(toolbar);
    add(table);
  }

  private String createShortDescription(TalkModel dbObject)
  {
    String desc = dbObject.getDescription();

    return creatShort(desc);
  }

  public static String creatShort(String desc)
  {
    if (desc == null)
    {
      return null;
    }
    int start = first(desc, "<br", desc.length());
    // start = first(desc, "<span", start);
    start = first(desc, "<ol", start);
    start = first(desc, "<ul", start);
    if (start > 0)
    {
      desc = desc.substring(0, start);
    }
    if (desc.length() > 256)
    {
      desc = desc.substring(0, 256);
    }
    return desc;
  }

  private static int first(String desc, String string, int start)
  {
    int current = desc.indexOf(string);
    if (current != -1)
    {
      return Math.min(current, start);
    }
    else
    {
      return start;
    }
  }
}
