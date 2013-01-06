package de.bitnoise.sonferenz.web.forms;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public abstract class KonferenzPanelWithForm extends Panel
{
  Form<String> _form;

  public KonferenzPanelWithForm(String id)
  {
    super(id);
  }

  @Override
  protected void onInitialize()
  {
    super.onInitialize();
    add(new FeedbackPanel("feedback").setOutputMarkupId(true));
    _form = new Form<String>("form") {
      @Override
      protected void onSubmit()
      {
        onSubmitForm();
      }
    };
    Button cancel = new Button("cancel") {
      public void onSubmit()
      {
        onCancel();
      }
    };
    cancel.setDefaultFormProcessing(false);
    _form.add(cancel);
    _form.add(new Button("submit"));
    initForm(_form);
    add(_form);
  }

  protected TextField<String> addTextInputField(String id, Model<String> model)
  {
    TextField<String> field = new TextField<String>(id, model);
    getForm().add(field);
    return field;
  }

  protected Form<String> getForm()
  {
    return _form;
  }

  abstract protected void initForm(Form<String> form);

  abstract protected void onSubmitForm();

  abstract protected void onCancel();

}
