package de.bitnoise.sonferenz.web.toolbar;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;

import de.bitnoise.sonferenz.web.action.IWebAction;

public class AddToolbarWithButton extends AbstractToolbar
{

  private IWebAction _action;

  public AddToolbarWithButton(String text, final DataTable<?,?> table,
      IWebAction action)
  {
    super(table);
    _action = action;
    WebMarkupContainer span = new WebMarkupContainer("span") {
      @Override
      protected void onComponentTag(ComponentTag tag)
      {
        tag.put("colspan", table.getColumns().size());
      }
    };
    AjaxFallbackLink link = new AjaxFallbackLink("link") {
      @Override
      public void onClick(AjaxRequestTarget target)
      {
        if (_action != null)
        {
          setResponsePage(_action.doAction(null));
        }
        else
        {
          onClickedOnAction(target);
        }
      }
    };
    Label linkText = new Label("linkText", text);
    add(span);
    span.add(link);
    span.setVisible(_action.isVisible());
    link.add(linkText);
  }

  protected void onClickedOnAction(AjaxRequestTarget target)
  {
  };
}
