package de.bitnoise.sonferenz.web.pages.config;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.ConfigurationModel;
import de.bitnoise.sonferenz.service.v2.services.ConfigurationService;
import de.bitnoise.sonferenz.web.forms.KonferenzPanelWithForm;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;

public class EditConfigEntry extends KonferenzPage
{
  @SpringBean
  UiFacade facade;

  @SpringBean
  ConfigurationService config;

  Model<String> keyModel;

  Model<String> valueModel;

  Integer _id;

  public EditConfigEntry(Integer id)
  {
    super();
    _id = id;
  }

  @Override
  protected void onInitialize()
  {
    super.onInitialize();
  }

  @Override
  protected Panel getPageContent(String id)
  {
    return new EditPanel(id);
  }

  class EditPanel extends KonferenzPanelWithForm
  {

    public EditPanel(String id)
    {
      super(id);
    }

    @Override
    protected void initForm(Form<String> form)
    {
      if (_id != null)
      {
        ConfigurationModel db = config.getById(_id);
        keyModel = Model.of(db.getName());
        valueModel = Model.of(db.getValueString());
      }
      else
      {
        keyModel = Model.of("");
        valueModel = Model.of("");
      }
      addTextInputField("key", keyModel);
      addTextInputField("value", valueModel);
    }

    @Override
    protected void onSubmitForm()
    {
      String neu = valueModel.getObject();
      config.saveStringValue(keyModel.getObject(), neu);
      setResponsePage(EditConfigurationPage.class);
    }

    @Override
    protected void onCancel()
    {
      setResponsePage(EditConfigurationPage.class);
    }
  }

}
