package de.bitnoise.sonferenz.web.pages.base;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NoRecordsToolbar;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import de.bitnoise.sonferenz.service.v2.services.ConfigurationService;
import de.bitnoise.sonferenz.web.component.SortableServiceDataProvider;
import de.bitnoise.sonferenz.web.component.TableBuilder;

public abstract class AbstractListPanel<VIEW_MODEL extends Serializable, DB_MODEL>
    extends Panel
{
  @SpringBean
  ConfigurationService config;
  
  boolean _hideOnEmpty;

  private DataTable<VIEW_MODEL,SortParam<String>> table;

  private String _defaultSort;

  public AbstractListPanel(String id, String headingId)
  {
    super(id);
    TableBuilder<VIEW_MODEL> builder = new TableBuilder<VIEW_MODEL>(headingId);
    initColumns(builder);
    SortableServiceDataProvider<DB_MODEL, VIEW_MODEL> provider = createProvider();
    List<IColumn<VIEW_MODEL,SortParam<String>>> columns = builder.getColumns();
    _defaultSort = builder.getDefaultSorting();
    Integer maxPageSize = config.getIntegerValue(100,
        "table." + headingId + ".paginationSize", 
        "table.paginationSize");
    table =  new DataTable<VIEW_MODEL,SortParam<String>>("contentTable", columns , provider, maxPageSize) ;
    addToolbars(table,provider);
    add(table);
    add(createAbovePanel("above"));
    setOutputMarkupId(true);
  }
  
  @Override
  protected void onConfigure()
  {
    super.onConfigure();
    if(_hideOnEmpty && getTotalCount(new PageRequest(0, 1)) == 0 ) {
      table.setVisibilityAllowed(false); 
    } else {
      table.setVisibilityAllowed(true); 
    }
  }

  protected void setHideWhenEmpty(boolean b)
  {
    _hideOnEmpty = b;
  }

  
  protected Component createAbovePanel(String id)
  {
    return new EmptyPanel(id);
  }


  protected void addToolbars(DataTable<VIEW_MODEL,SortParam<String>> table, SortableServiceDataProvider<DB_MODEL, VIEW_MODEL> provider)
  {
    table.addTopToolbar(new NavigationToolbar(table));
    table.addTopToolbar(new HeadersToolbar(table, provider));
    table.addBottomToolbar(new NoRecordsToolbar(table));
  }

  protected SortableServiceDataProvider<DB_MODEL, VIEW_MODEL> createProvider()
  {
    return new SortableServiceDataProvider<DB_MODEL, VIEW_MODEL>()
    {
      @Override
      protected Page<DB_MODEL> getAllItems(PageRequest request)
      {
        return getItems(request);
      }

      @Override
      public long size()
      {
        Long l = getTotalCount(new PageRequest(1, 1));
        return l.intValue();
      }

      @Override
      protected VIEW_MODEL transferType(DB_MODEL dbObject)
      {
        return transferDbToViewModel(dbObject);
      }
      
      @Override
      protected Sort createDefaultSorting()
      {
        return createDefaultSort();
      }
    };
  }

  protected Sort createDefaultSort() {
     if(_defaultSort==null) {
       return null;
     }
     return new Sort(Direction.ASC,_defaultSort);
  }

  protected abstract VIEW_MODEL transferDbToViewModel(DB_MODEL dbObject);

  protected abstract Page<DB_MODEL> getItems(PageRequest request);

  abstract protected void initColumns(TableBuilder<VIEW_MODEL> builder);

  protected Long getTotalCount(PageRequest request)
  {
    Page<DB_MODEL> items = getItems(request);
    if (items == null)
    {
      return 0L;
    }
    return items.getTotalElements();
  }

}
