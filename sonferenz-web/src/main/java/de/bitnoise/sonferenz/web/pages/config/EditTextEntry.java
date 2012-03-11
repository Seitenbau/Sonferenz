package de.bitnoise.sonferenz.web.pages.config;

import javax.security.auth.callback.TextInputCallback;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.visural.common.web.HtmlSanitizer;
import com.visural.wicket.component.nicedit.RichTextEditorFormBehavior;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.StaticContentModel;
import de.bitnoise.sonferenz.service.v2.services.StaticContentService;
import de.bitnoise.sonferenz.web.component.rte.ReducedRichTextEditor;
import de.bitnoise.sonferenz.web.forms.KonferenzPanelWithForm;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;

public class EditTextEntry extends KonferenzPage
{
  @SpringBean
  UiFacade facade;

  @SpringBean
  StaticContentService texte;

  Model<String> keyModel;

  Model<String> valueModel;

  Integer _id;

  public EditTextEntry(Integer id)
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

    private Boolean allowHTML;

    public EditPanel(String id)
    {
      super(id);
    }

    @Override
    protected void initForm(Form<String> form)
    {
      if (_id != null)
      {
        StaticContentModel db = texte.getById(_id);
        allowHTML = db.getAllowHtml();
        keyModel = Model.of(db.getName());
        valueModel = Model.of(db.getHtml());
      }
      else
      {
        keyModel = Model.of("");
        valueModel = Model.of("");
      }
      addTextInputField("key", keyModel);
      if(allowHTML!=null && allowHTML) 
      {
	      form.add(new RichTextEditorFormBehavior());
	      ReducedRichTextEditor rte = new ReducedRichTextEditor("value", valueModel);
	      form.add(rte);
      } else {
    	  TextArea<String> field = new TextArea<String>("value", valueModel);
    	  form.add(field);
      }
    }

    @Override
    protected void onSubmitForm()
    {
      String neu = valueModel.getObject();
      if (neu != null)
      {
        if (neu.endsWith("<br>"))
        {
          neu = neu.substring(0, neu.length() - 4);
        } 
      }
      texte.saveText(keyModel.getObject(), neu);
      getLocalizer().clearCache();
      setResponsePage(EditTextePage.class);
    }

    @Override
    protected void onCancel()
    {
      setResponsePage(EditTextePage.class);
    }
  }

}
