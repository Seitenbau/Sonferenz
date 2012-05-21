package de.bitnoise.sonferenz.web.pages.suggestion;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class LikeColumn<T> 
extends AbstractColumn<T>
{

  public LikeColumn(IModel<String> displayModel)
  {
    super(displayModel,"likes");
  }

  public void populateItem(Item<ICellPopulator<T>> cellItem,
      String componentId, final IModel<T> rowModel)
  {
    cellItem.add(new LikePanel(componentId, LikeColumn.this.getLike(rowModel),LikeColumn.this.getCount(rowModel)) {
      @Override
      protected void onChange(AjaxRequestTarget target,Model<Boolean> newValue)
      {
        LikeColumn.this.onChange(target,rowModel, newValue);
      }
    });
  }

  protected void onChange(AjaxRequestTarget target,IModel<T> rowModel, Model<Boolean> newValue)
  {

  }

  protected boolean getLike(IModel<T> rowModel)
  {
    return false;
  }
  
  protected Integer getCount(IModel<T> rowModel)
  {
    return 0;
  }
}
