package de.bitnoise.sonferenz.web.forms;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;

import de.bitnoise.sonferenz.web.forms.fields.Field;

public class KonferenzContentPanel extends Panel
{
  FeedbackPanel _feedback;
  Label _legend;
  List<Field> _fields = new ArrayList<Field>();
  FormCallback _callback;

  public KonferenzContentPanel(FormCallback callback, String id)
  {
    super(id);
    _callback = callback;
  }

  public void lazyAddField(Field field)
  {
    _fields.add(field);
  }

  public void setLegend(String name)
  {
    _legend = new Label("legend", Model.of(name));
  }

  @Override
  protected void onInitialize()
  {
    super.onInitialize();

    _feedback = new FeedbackPanel("feedback");
    if (_legend == null)
    {
      _legend = new Label("legend", Model.of(""));
    }

    Form<String> form = new Form<String>("form")
    {
      @Override
      protected void onSubmit()
      {
        _callback.onSubmitForm(this);
      }
    };
    Button cancel = new Button("cancel")
    {
      public void onSubmit()
      {
        _callback.onCancelForm(this);
      }
    };
    Button submit = new Button("submit");

    add(_feedback.setOutputMarkupId(true));

    add(form);
    form.add(_legend);
    form.add(submit);
    form.add(cancel);

    RepeatingView view = new RepeatingView("repeaterFields");
    for (Field field : _fields)
    {
      String next = view.newChildId();
      view.add(field.createPanel(next));
    }
    form.add(view);
  }


}
