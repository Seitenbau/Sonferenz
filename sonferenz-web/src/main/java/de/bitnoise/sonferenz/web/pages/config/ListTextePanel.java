package de.bitnoise.sonferenz.web.pages.config;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.StaticContentModel;
import de.bitnoise.sonferenz.service.v2.services.StaticContentService;
import de.bitnoise.sonferenz.web.component.TableBuilder;
import de.bitnoise.sonferenz.web.component.TableBuilder.ActionColumn;
import de.bitnoise.sonferenz.web.component.link.AjaxLink;
import de.bitnoise.sonferenz.web.pages.base.AbstractListPanel;
import de.bitnoise.sonferenz.web.pages.base.IconPanel;
import de.bitnoise.sonferenz.web.pages.base.IconPanel.Type;

class TexteViewModel implements Serializable
{
  Integer id;

  String name;

  String value;
}

public class ListTextePanel extends
    AbstractListPanel<TexteViewModel, StaticContentModel>
{
  @SpringBean
  UiFacade facade;

  @SpringBean
  StaticContentService texte;

  public ListTextePanel(String id)
  {
    super(id, "TexteTable");
  }

  @Override
  protected void initColumns(TableBuilder<TexteViewModel> builder)
  {
    builder.addActions("action", new Edit());
    builder.addColumn("name");
    builder.addColumn("value");
    builder.setDefaultSort("name");
  }

  @Override
  protected Component createAbovePanel(String id)
  {
    AjaxLink btn = new AjaxLink(id, "config.texte.add") 
    {
      @Override
      protected void onClickLink(AjaxRequestTarget target)
      {
        setResponsePage(new EditTextEntry(null));
      }
    };
    return btn;
  }

  class Edit extends ActionColumn<TexteViewModel>
  {
    @Override
    public Component populate(String id, final TexteViewModel row)
    {
      return new IconPanel(id, Type.EDIT)
      {
        @Override
        protected void onClick(AjaxRequestTarget target)
        {
          setResponsePage(new EditTextEntry(row.id));
        }
      };
    }
  }

  @Override
  protected TexteViewModel transferDbToViewModel(StaticContentModel dbObject)
  {
    TexteViewModel view = new TexteViewModel();
    view.id = dbObject.getId();
    view.name = dbObject.getName();
    view.value = dbObject.getHtml();
    if (view.value != null && view.value.length() > 100)
    {
      view.value = view.value.substring(0, 100);
    }
    return view;
  }

  @Override
  protected Page<StaticContentModel> getItems(PageRequest request)
  {
    return facade.getTexte(request);
  }

}
