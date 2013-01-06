package de.bitnoise.sonferenz.web.pages.suggestion;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import com.visural.common.web.HtmlSanitizer;
import com.visural.wicket.behavior.inputhint.InputHintBehavior;
import com.visural.wicket.component.confirmer.ConfirmerLink;
import com.visural.wicket.component.nicedit.RichTextEditorFormBehavior;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.SuggestionModel;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.web.component.rte.ReducedRichTextEditor;
import de.bitnoise.sonferenz.web.pages.users.FormPanel;

public class EditWhishPanel extends FormPanel
{
  final Model<String> modelTitle = new Model<String>();
  Model<String> modelDesc = new Model<String>();
  Model<UserModel> modelUser = new Model<UserModel>();

  
  @SpringBean
  UiFacade facade;

  private SuggestionModel _talk;

  public EditWhishPanel(String id, SuggestionModel talk)
  {
    super(id);
    InjectorHolder.getInjector().inject(this);
    _talk = talk;
  }

  @Override
  protected void onInitialize()
  {
    super.onInitialize();

    // Prepare model
    modelTitle.setObject(_talk.getTitle());
    modelDesc.setObject(_talk.getDescription());
    modelUser.setObject(_talk.getOwner());

    // Bind Wicket Elements
    addFeedback(this, "feedback");

    Form<String> form = new Form<String>("form") {
      @Override
      protected void onSubmit()
      {
        clickedOnSave();
      }
    };

    Button cancel = new Button("cancel") {
      public void onSubmit()
      {
        setResponsePage(WhishOverviewPage.class);
      }
    };
    cancel.setDefaultFormProcessing(false);
    form.add(cancel);

    FormComponent<String> titleField = new TextField<String>("title", modelTitle);
    titleField
        .setRequired(true)
        .add(StringValidator.maximumLength(254))
        .add(new InputHintBehavior(form, "Kurz und pr\u00e4gnant", "color: #aaa;"));
        ;
    
    // List<? extends UserModel> choices = userService.listAllUsers();
    LoadableDetachableModel choices = new LoadableDetachableModel() {
      @Override
      protected Object load()
      {
        return facade.getAllUsers();
      }
    };
    IChoiceRenderer<UserModel> render = new IChoiceRenderer<UserModel>() {
      public Object getDisplayValue(UserModel object)
      {
        return object.toString();
      }

      public String getIdValue(UserModel object, int index)
      {
        return Integer.toString(object.getId());
      }
    };
    DropDownChoice<UserModel> ddc = new DropDownChoice<UserModel>("theOwner",
        modelUser, choices, render);
    ddc.setRequired(true);

    ConfirmerLink  btnDel = new ConfirmerLink("delete") {
      
      public void onClick()
      {
        facade.deleteSuggestion(_talk);
        setResponsePage(WhishOverviewPage.class);
      }
    };
    if (_talk.isNew())
    {
      btnDel.setVisible(false);
    }
    ReducedRichTextEditor rtf = new ReducedRichTextEditor("description", modelDesc);
    rtf.add(StringValidator.maximumLength(1000 * 5));
    form.add(titleField);
    form.add(new RichTextEditorFormBehavior());
    form.add(rtf);
//    form.add(new TextArea<String>("description", modelDesc));
    form.add(ddc);
    form.add(btnDel);
    form.add(new Button("submit"));
    add(form);
  }


  protected void clickedOnSave()
  {
    String valueTitle = modelTitle.getObject();
    String valueDescRaw = modelDesc.getObject();
    String valueDesc = HtmlSanitizer.sanitize(valueDescRaw);
    UserModel valueUser = modelUser.getObject();
    _talk.setOwner(valueUser);
    _talk.setTitle(valueTitle);
    _talk.setDescription(valueDesc);
    facade.saveSuggestion(_talk);
    setResponsePage(WhishOverviewPage.class);
  }

}
