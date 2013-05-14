package de.bitnoise.sonferenz.web.pages.talks;

import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.service.v2.services.StaticContentService;
import de.bitnoise.sonferenz.service.v2.services.TalkService;
import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.component.SortableServiceDataProvider;
import de.bitnoise.sonferenz.web.component.TableBuilder;
import de.bitnoise.sonferenz.web.pages.talks.action.EditOrViewTalk;

public class ListTalksPanel extends Panel
{
  @SpringBean
  StaticContentService content2;
  
  @SpringBean
  TalkService talks;
  
  
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

    ISortableDataProvider<ModelTalkList,SortParam<String>> provider = new SortableServiceDataProvider<TalkModel, ModelTalkList>( )
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
        ConferenceModel conf = KonferenzSession.get().getCurrentConference();
        return talks.getTalks(conf,request);
      }

//      @Override
//      public long size()
//      {
//        return (int) talks.getAllTalksCount();
//      }

      @Override
      protected Sort createDefaultSorting()
      {
        return new Sort(new Order(Direction.ASC, "title"));
      }

    };
    String text=content2.text("page.talkHeader","");
    add(new Label("headerText",text).setEscapeModelStrings(false));
    DefaultDataTable<ModelTalkList,SortParam<String>> table = new DefaultDataTable<ModelTalkList,SortParam<String>>(
        "talkTable", builder.getColumns(), provider, 100);
    
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
