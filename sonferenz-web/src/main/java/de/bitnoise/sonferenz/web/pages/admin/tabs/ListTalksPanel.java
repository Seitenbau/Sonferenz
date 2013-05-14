package de.bitnoise.sonferenz.web.pages.admin.tabs;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.model.SpeakerModel;
import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.service.v2.services.SpeakerService;
import de.bitnoise.sonferenz.service.v2.services.TalkService;
import de.bitnoise.sonferenz.web.action.WebAction;
import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.component.SortableServiceDataProvider;
import de.bitnoise.sonferenz.web.component.TableBuilder;
import de.bitnoise.sonferenz.web.component.link.AjaxLink;
import de.bitnoise.sonferenz.web.component.panels.KonferenzTabPanel;
import de.bitnoise.sonferenz.web.pages.speakers.PageSpeaker;

public class ListTalksPanel extends KonferenzTabPanel
{

  @SpringBean
  TalkService talkService;

  public ListTalksPanel(String id)
  {
    super(id);
    InjectorHolder.getInjector().inject(this);
  }

  @Override
  protected void onInitialize()
  {
    super.onInitialize();

    TableBuilder<TalkListItem> builder = new TableBuilder<TalkListItem>("talks") {
      {
        addColumn(new Column() {
          {
            setTitle("Titel");
            setModelProperty("title");
            sortable();
            action(new EditTalk());
          }
        });
        addColumn(new Column() {
          {
            setTitle("Speaker1");
            setModelProperty("speaker1");
            action(new EditSpeaker(0));
          }
        });
        addColumn(new Column() {
          {
            setTitle("Speaker2");
            setModelProperty("speaker2");
            action(new EditSpeaker(1));
          }
        });
        
      }
    };

    SortableServiceDataProvider<TalkModel, TalkListItem> provider = createProvider();
    DefaultDataTable<TalkListItem, SortParam<String>> table = new DefaultDataTable<TalkListItem, SortParam<String>>(
        "talksTable", builder.getColumns(), provider, 100);

    add(new AjaxLink("above", "table.talks.createLink") {
      @Override
      protected void onClickLink(AjaxRequestTarget target)
      {
        setResponsePage(new PageSpeaker((SpeakerModel) null));
      }
    });
    add(table);
  }

  private SortableServiceDataProvider<TalkModel, TalkListItem> createProvider()
  {
    SortableServiceDataProvider<TalkModel, TalkListItem> provider = new SortableServiceDataProvider<TalkModel, TalkListItem>()
    {
      @Override
      protected TalkListItem transferType(TalkModel dbObject)
      {
        TalkListItem user = new TalkListItem();
        user.title = dbObject.getTitle();
        user.speakers = dbObject.getSpeakers();
        user.talk = dbObject;
        return user;
      }

      @Override
      protected Page<TalkModel> getAllItems(PageRequest request)
      {
        ConferenceModel conf = KonferenzSession.get().getCurrentConference();
        return talkService.getTalks(conf, request);
      }

      @Override
      protected Sort createDefaultSorting()
      {
        return new Sort(Direction.ASC, "title");
      }

    };
    return provider;
  }

  public class TalkListItem implements Serializable
  {
    public String title;
    public List<SpeakerModel> speakers;
    public TalkModel talk;

    public String getSpeaker1()
    {
      if (speakers.size() > 0)
      {
        return speakers.get(0).getName();
      }
      return null;
    }

    public String getSpeaker2()
    {
      if (speakers.size() > 1)
      {
        return speakers.get(1).getName();
      }
      return null;
    }
  }

  public class EditSpeaker extends WebAction<IModel<TalkListItem>>
  {

    private int idx;

    public EditSpeaker(int i)
    {
      idx =i;
    }

    public org.apache.wicket.Page doAction(IModel<TalkListItem> model)
    {
      return new PageSpeaker(model.getObject().speakers.get(idx));
    }

  }
  public class EditTalk extends WebAction<IModel<TalkListItem>>
  {
    
    public org.apache.wicket.Page doAction(IModel<TalkListItem> model)
    {
      return null;
    }
    
  }

}
