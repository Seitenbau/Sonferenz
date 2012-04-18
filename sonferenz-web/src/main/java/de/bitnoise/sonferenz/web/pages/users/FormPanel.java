package de.bitnoise.sonferenz.web.pages.users;

import java.util.Date;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.visural.wicket.behavior.dateinput.DateInputBehavior;
import com.visural.wicket.behavior.dateinput.DateInputFormat;
import com.visural.wicket.behavior.inputhint.InputHintBehavior;

public class FormPanel extends Panel
{
  public FormPanel(String id)
  {
    super(id);
  }

  public FormPanel(String id, IModel<?> model)
  {
    super(id, model);
  }

  private static final String DATE_FORMAT = "dd.mm.yyyy";

  protected void addFeedback(MarkupContainer page, String id)
  {
    page.add(new FeedbackPanel(id) .setOutputMarkupId(true));
  }

  protected TextField<String> addTextInputField(Form<String> form, String id,
      Model<String> model, boolean required)
  {
    TextField<String> field = new TextField<String>(id, model);
	form.add(field.setRequired(required));
	return field;
  }

  protected void addTextInputField(Form<String> form, String id,
      Model<String> model)
  {
    addTextInputField(form, id, model, false);
  }

  protected void addDateInputField(Form<String> form, String id,
      Model<Date> model)
  {
    Component fFrom = new DateTextField(id, model, DATE_FORMAT);
    fFrom.add(new DateInputBehavior()
        .setDateFormat(DateInputFormat.DD_MM_YYYY_DOTS));
    fFrom.add(new InputHintBehavior(form, DATE_FORMAT, "color: #aaa;"));
    form.add(fFrom);
  }

  protected void addTextLabel(MarkupContainer page, String id, String content,
      boolean escapeHtml)
  {
    page.add(new Label(id, Model.of(content)).setEscapeModelStrings(escapeHtml));
  }

}
