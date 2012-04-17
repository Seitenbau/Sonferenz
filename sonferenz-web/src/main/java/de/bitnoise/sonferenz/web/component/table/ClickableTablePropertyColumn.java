package de.bitnoise.sonferenz.web.component.table;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import de.bitnoise.sonferenz.web.action.ActionBookmark;
import de.bitnoise.sonferenz.web.action.IWebAction;
import de.bitnoise.sonferenz.web.pages.AclControlled;

public class ClickableTablePropertyColumn<T> extends AbstractColumn<T>
{

  private static final long serialVersionUID = -1L;
  private final String property;
  private IWebAction _action;

  public ClickableTablePropertyColumn(IModel<String> displayModel,
      String property)
  {
    this(displayModel, property, null, null);
  }

  public ClickableTablePropertyColumn(IModel<String> displayModel,
      String property, IWebAction action)
  {
    this(displayModel, property, null, action);
  }

  public ClickableTablePropertyColumn(IModel<String> displayModel,
      String property, String sort)
  {
    super(displayModel, sort);
    this.property = property;
  }

  public ClickableTablePropertyColumn(IModel<String> displayModel,
      String property, String sort, IWebAction action)
  {
    super(displayModel, sort);
    this.property = property;
    _action = action;
  }

  public void populateItem(Item<ICellPopulator<T>> cellItem,
      String componentId, IModel<T> rowModel)
  {
    boolean access = true;
    if (_action instanceof AclControlled)
    {
      access = false;
      AclControlled<T> act = (AclControlled<T>) _action;
      if (act.canAccess(rowModel))
      {
        access = true;
      }
    }
    cellItem.add(new AttributeAppender("class", true, Model.of("column_"
        + cellItem.getIndex()), " "));
    PropertyModel<Object> labelModel = new PropertyModel<Object>(rowModel, property);
	if (access)
    {
      LinkPanel linkPanel = new LinkPanel(_action, componentId, rowModel, labelModel);
	  cellItem.add(linkPanel);
    }
    else
    {
      cellItem.add(new Label(componentId, labelModel));
    }
  }

  protected void onClick(IModel<T> clicked)
  {
  }

  private class LinkPanel extends Panel
  {

    public LinkPanel(IWebAction action, String id, IModel<T> rowModel,
        IModel<?> labelModel)
    {
      super(id);
      Link<T> link = null;
      if (action instanceof ActionBookmark)
      {
        link = ((ActionBookmark<T>) action).createBookmark(rowModel,"link");
      }
      else
      {
        link = new Link<T>("link", rowModel) {
          @Override
          public void onClick()
          {
            if (_action == null)
            {
              ClickableTablePropertyColumn.this.onClick(getModel());
            }
            else
            {
              setResponsePage(_action.doAction(getModel()));
            }
          }

        };
      }
      add(link);
      link.add(new Label("label", labelModel));
    }
  }

}
