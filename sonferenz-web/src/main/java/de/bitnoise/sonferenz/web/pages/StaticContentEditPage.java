package de.bitnoise.sonferenz.web.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.visural.wicket.aturl.At;

import wicket.contrib.tinymce.TinyMceBehavior;
import wicket.contrib.tinymce.settings.TinyMCESettings;
import wicket.contrib.tinymce.settings.TinyMCESettings.Align;
import wicket.contrib.tinymce.settings.TinyMCESettings.Location;
import wicket.contrib.tinymce.settings.TinyMCESettings.Theme;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.web.pages.statics.ConferencePage;

@At(url = "/editnode", urlParameters = { "id" })
public class StaticContentEditPage extends KonferenzPage
{
  @SpringBean
  UiFacade facade;

  private String _id;

  public StaticContentEditPage(PageParameters params)
  {
    super();
    if (params != null)
    {
    	_id = params.getString("id");
    }
  }

  @Override
  protected void onInitialize()
  {
    super.onInitialize();
  }

  @Override
  protected Panel getPageContent(String id)
  {
    if (_id != null)
    {
      String html = facade.getText(_id);
      if (html == null)
      {
        html = "";
      }
      return new EditPanel(id, _id, html){
		@Override
		protected void onSuccess() {
			setResponsePage(ConferencePage.class);
		}
      };
    }
    return super.getPageContent(id);
  }

  abstract class EditPanel extends Panel
  {
    String _key;

    String _conent;

    Model<String> formContent = new Model<String>();

    public EditPanel(String id, String key, String content)
    {
      super(id);
      _key = key;
      _conent = content;
    }

    @Override
    protected void onInitialize()
    {
      super.onInitialize();
      formContent.setObject(_conent);
      Form<String> form = new Form<String>("form")
      {
        @Override
        protected void onSubmit()
        {
          clickedOnSave();
        }
      };

      TextArea<String> field = new TextArea<String>("content", formContent); 
//      TinyMCESettings settings=new TinyMCESettings(Theme.advanced);
//      settings.setToolbarAlign(Align.left);
//      settings.setStatusbarLocation(Location.top);
//      settings.setRelativeUrls(false);
//      TinyMceBehavior tinyMceBehavior = new TinyMceBehavior(settings);
//      field.add(tinyMceBehavior);
      
      form.add(field);
      form.add(new Button("submit"));
      add(form);
    }

    protected void clickedOnSave()
    {
      String neu = formContent.getObject();
      if(neu !=null ) {
        facade.saveText(_id,neu);
      }
      onSuccess();
    }
    
    abstract protected void onSuccess();
  }
}
