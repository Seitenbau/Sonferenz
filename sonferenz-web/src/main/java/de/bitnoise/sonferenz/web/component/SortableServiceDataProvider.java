package de.bitnoise.sonferenz.web.component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import de.bitnoise.sonferenz.service.v2.exceptions.GeneralConferenceException;
import de.bitnoise.sonferenz.web.pages.admin.tabs.ListSpeakerPanel.SpeakerListItem;

public abstract class SortableServiceDataProvider<TYPE_DB, TYPE_UI extends Serializable>
    extends SortableDataProvider<TYPE_UI,SortParam<String>>
{
  public SortableServiceDataProvider(String columToSort)
  {
    super();
    setSort(new SortParam(columToSort, true));
  }
  public SortableServiceDataProvider( )
  {
    super();
  }

  @Override
  public Iterator<? extends TYPE_UI> iterator(long first, long count) {
    boolean ascending = true;
    String prop = null;
    if (getSort() != null)
    {
      ascending = getSort().isAscending();
      prop = getSort().getProperty().getProperty();
    }
    List<TYPE_UI> daten = loadListFromBackend(first, count, ascending, prop);
    return daten.iterator();
  }

  protected List<TYPE_UI> loadListFromBackend(long first, long count,
      boolean ascending, String propertyToSort)
      throws GeneralConferenceException
  {
    List<TYPE_DB> rawUsers = getAllItemList(first, count,
        propertyToSort, ascending);
    List<TYPE_UI> result = new ArrayList<TYPE_UI>();
    for (TYPE_DB rawUser : rawUsers)
    {
      TYPE_UI conf = transferType(rawUser);
      if (conf != null)
      {
        result.add(conf);
      }
    }
    return result;
  }

  protected List<TYPE_DB> getAllItemList(long first, long c, String propertyToSort, boolean ascending)
  {
	  int f = (int) (first/c);
	  int count = (int) c;
    PageRequest request;
    if(propertyToSort!=null) {
      Direction direction=Direction.DESC;
      if(ascending) {
        direction=Direction.ASC;
      }
      Sort sort=new Sort(new Order(direction, propertyToSort));
      request = new PageRequest(f, count,sort);
    } else {
      Sort sort=createDefaultSorting();
      if(sort!=null) {
        request = new PageRequest(f, count, sort);
      } else {
        request = new PageRequest(f, count);
      }
    }
    Page<TYPE_DB> result = getAllItems(request);
    return result.getContent();
  }

  protected abstract Sort createDefaultSorting();
  
  protected abstract Page<TYPE_DB> getAllItems(PageRequest request);

  public long size() {
    Long s = getAllItems(new PageRequest(0, 1)).getTotalElements();
    return s.intValue();
  }

  public IModel<TYPE_UI> model(TYPE_UI object)
  {
    return Model.of(object);
  }

  abstract protected TYPE_UI transferType(TYPE_DB dbObject);
}
