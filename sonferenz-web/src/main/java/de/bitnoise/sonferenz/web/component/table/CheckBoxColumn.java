package de.bitnoise.sonferenz.web.component.table;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

public abstract class CheckBoxColumn<T> extends AbstractColumn<T,SortParam<String>>
{
  public CheckBoxColumn(IModel<String> displayModel)
  {
    super(displayModel);
  }

  public void populateItem(Item<ICellPopulator<T>> cellItem,
      String componentId, IModel<T> rowModel)
  {
    cellItem.add(new CheckPanel(componentId, newCheckBoxModel(rowModel)));
  }

  protected CheckBox newCheckBox(String id, IModel<Boolean> checkModel)
  {
    return new AjaxCheckBox("check", checkModel)
    {
      @Override
      protected void onUpdate(AjaxRequestTarget target)
      {
//        if (getModelObject()) {
//          getModel().setObject(true);
          this.setModelObject(!getModelObject());
          this.setOutputMarkupId(true);
          target.add(this);
//      } 
      }
    };
  }

  protected abstract IModel<Boolean> newCheckBoxModel(IModel<T> rowModel);

  private class CheckPanel extends Panel
  {
    public CheckPanel(String id, IModel<Boolean> checkModel)
    {
      super(id);
      add(newCheckBox("check", checkModel));
    }
  }
}