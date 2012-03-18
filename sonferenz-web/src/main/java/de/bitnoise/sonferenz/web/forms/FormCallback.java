package de.bitnoise.sonferenz.web.forms;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

public interface FormCallback
{

  void onSubmitForm(Form target);

  void onCancelForm(Form target);

}
