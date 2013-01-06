package de.bitnoise.sonferenz.web.forms;

import java.io.Serializable;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.html.panel.Panel;

import de.bitnoise.sonferenz.web.forms.fields.StringInput;

public abstract class KonferenzForm implements Serializable, FormCallback
{
  
  public KonferenzForm() {
    InjectorHolder.getInjector().inject(this);
  }

  public Panel createPanel(String id)
  {
    KonferenzContentPanel panel = new KonferenzContentPanel(getFormCallback(), id);
    createForm(new FormBuilder(panel));
    return panel;
  }

  protected FormCallback getFormCallback()
  {
    return this;
  }

  protected abstract void createForm(FormBuilder builder);

  public class FormBuilder
  {
    KonferenzContentPanel _panel;

    public FormBuilder(KonferenzContentPanel panel)
    {
      _panel = panel;
    }

    public StringInput addStringInputField(String labelName)
    {
      StringInput field = new StringInput(labelName);
      _panel.lazyAddField(field);
      return field;
    }

    public void setLegend(String name)
    {
      _panel.setLegend(name);
    }
  }

}
