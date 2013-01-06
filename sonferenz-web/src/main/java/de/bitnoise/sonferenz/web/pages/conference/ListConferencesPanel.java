package de.bitnoise.sonferenz.web.pages.conference;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.web.component.SortableServiceDataProvider;
import de.bitnoise.sonferenz.web.component.TableBuilder;
import de.bitnoise.sonferenz.web.pages.conference.action.AddConference;
import de.bitnoise.sonferenz.web.pages.conference.action.EditConference;
import de.bitnoise.sonferenz.web.pages.conference.table.ConferenceListItem;
import de.bitnoise.sonferenz.web.toolbar.AddToolbarWithButton;

public class ListConferencesPanel extends Panel
{
  @SpringBean
  private UiFacade facade;

  public ListConferencesPanel(String id)
  {
    super(id);
    InjectorHolder.getInjector().inject(this);
  }

  @Override
  protected void onInitialize()
  {
    super.onInitialize();
    TableBuilder<ConferenceListItem> builder = new TableBuilder<ConferenceListItem>("conference") {
      {
        addColumn(new Column() {
          {
            setTitle("Aktiv");
            setModelProperty("aktiv");
          }
        });
        addColumn(new Column() {
          {
            setTitle("Status");
            setModelProperty("state");
            sortable();
          }
        });
        addColumn(new Column() {
          {
            setTitle("Name");
            setModelProperty("shortName");
            sortable();
            action(new EditConference());
          }
        });
      }
    };

    ISortableDataProvider<ConferenceListItem,SortParam<String>> provider = 
        new SortableServiceDataProvider<ConferenceModel, ConferenceListItem>() {
      @Override
      protected ConferenceListItem transferType(ConferenceModel dbObject)
      {
        ConferenceListItem item = new ConferenceListItem();
        item.shortName = dbObject.getShortName();
        item.state = dbObject.getState();
        item.aktiv = dbObject.getActive();
        item.conference = dbObject;
        return item;
      }

      @Override
      protected Page<ConferenceModel> getAllItems(PageRequest request)
      {
        return facade.getConferences(request);
      }

      @Override
      public long size()
      {
        return facade.getAllConferencesCount();
      }
      
      @Override
      protected Sort createDefaultSorting()
      {
        return new Sort(new Order(Direction.ASC, "shortName"));
      }
    };
    DefaultDataTable<ConferenceListItem,SortParam<String>> table = new DefaultDataTable<ConferenceListItem,SortParam<String>>(
        "conferenceTable", builder.getColumns(), provider, 20);
    
	AddToolbarWithButton toolbar = new AddToolbarWithButton(
				"+ create Conference", table, new AddConference());
	table.addBottomToolbar(toolbar);

	add(table);
  }
}
