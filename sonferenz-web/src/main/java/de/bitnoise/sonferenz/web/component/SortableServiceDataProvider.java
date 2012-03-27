package de.bitnoise.sonferenz.web.component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import de.bitnoise.sonferenz.service.v2.exceptions.GeneralConferenceException;

public abstract class SortableServiceDataProvider<TYPE_DB, TYPE_UI extends Serializable>
    extends SortableDataProvider<TYPE_UI>
{
  public SortableServiceDataProvider(String columToSort)
  {
    super();
    setSort(columToSort, true);
  }
  public SortableServiceDataProvider( )
  {
    super();
  }

  public Iterator<? extends TYPE_UI> iterator(int first, int count)
  {
    boolean ascending = true;
    String prop = null;
    if (getSort() != null)
    {
      ascending = getSort().isAscending();
      prop = getSort().getProperty();
    }
    List<TYPE_UI> daten = loadListFromBackend(first, count, ascending, prop);
    return daten.iterator();
  }

  protected List<TYPE_UI> loadListFromBackend(int first, int count,
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

  protected List<TYPE_DB> getAllItemList(int first, int count, String propertyToSort, boolean ascending)
  {
    int f = first/count;
    PageRequest request;
    if(propertyToSort!=null) {
      Direction direction=Direction.DESC;
      if(ascending) {
        direction=Direction.ASC;
      }
      Sort sort=new Sort(new Order(direction, propertyToSort));
      request = new PageRequest(f, count,sort);
    } else {
      request = new PageRequest(f, count);
    }
    Page<TYPE_DB> result = getAllItems(request);
    return result.getContent();
  }

  protected abstract Page<TYPE_DB> getAllItems(PageRequest request);

  public abstract int size();

  public IModel<TYPE_UI> model(TYPE_UI object)
  {
    return Model.of(object);
  }

  abstract protected TYPE_UI transferType(TYPE_DB dbObject);
}
