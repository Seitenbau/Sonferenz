package de.bitnoise.sonferenz.web.pages.config;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.ConfigurationModel;
import de.bitnoise.sonferenz.service.v2.services.StaticContentService;
import de.bitnoise.sonferenz.web.component.TableBuilder;
import de.bitnoise.sonferenz.web.component.TableBuilder.ActionColumn;
import de.bitnoise.sonferenz.web.component.link.AjaxLink;
import de.bitnoise.sonferenz.web.pages.base.AbstractListPanel;
import de.bitnoise.sonferenz.web.pages.base.IconPanel;
import de.bitnoise.sonferenz.web.pages.base.IconPanel.Type;

class ConfigViewModel implements Serializable
{
  Integer id;

  String name;

  String value;
}

public class ListConfigPanel extends
    AbstractListPanel<ConfigViewModel, ConfigurationModel>
{
  @SpringBean
  StaticContentService texte;

  public ListConfigPanel(String id)
  {
    super(id, "GlobalConfig");
  }

  @SpringBean
  UiFacade facade;

  @Override
  protected void initColumns(TableBuilder<ConfigViewModel> builder)
  {
    builder.addActions("actions", new Edit());
    builder.addColumn("name");
    builder.addColumn("value");
    builder.setDefaultSort("name");
  }
  

  @Override
  protected Component createAbovePanel(String id)
  {
    AjaxLink btn = new AjaxLink(id, texte.text("config.values.add","Add new key"))
    {
      @Override
      protected void onClickLink(AjaxRequestTarget target)
      {
        setResponsePage(new EditConfigEntry(null));
      }
    };
    return btn;
  }

  class Edit extends ActionColumn<ConfigViewModel>
  {
    @Override
    public Component populate(String id, final ConfigViewModel row)
    {
      return new IconPanel(id, Type.EDIT)
      {
        @Override
        protected void onClick(AjaxRequestTarget target)
        {
          setResponsePage(new EditConfigEntry(row.id));
        }
      };
    }
  }

  @Override
  protected ConfigViewModel transferDbToViewModel(ConfigurationModel dbObject)
  {
    ConfigViewModel view = new ConfigViewModel();
    view.name = dbObject.getName();
    view.value = dbObject.getValueString();
    view.id = dbObject.getId();
    return view;
  }

  @Override
  protected Page<ConfigurationModel> getItems(PageRequest request)
  {
    return facade.getAllConfigurations(request);
  }

}
