package de.bitnoise.sonferenz.web.forms;

import org.apache.wicket.markup.html.form.Form;

public interface FormCallback
{

  void onSubmitForm(Form target);

  void onCancelForm(Form target);

}
