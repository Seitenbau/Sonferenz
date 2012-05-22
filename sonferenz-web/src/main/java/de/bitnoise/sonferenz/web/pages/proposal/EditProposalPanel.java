package de.bitnoise.sonferenz.web.pages.proposal;

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
import org.apache.wicket.validation.validator.StringValidator.MaximumLengthValidator;

import com.visural.common.web.HtmlSanitizer;
import com.visural.wicket.behavior.inputhint.InputHintBehavior;
import com.visural.wicket.component.confirmer.ConfirmerLink;
import com.visural.wicket.component.nicedit.RichTextEditorFormBehavior;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.ProposalModel;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.web.component.rte.ReducedRichTextEditor;
import de.bitnoise.sonferenz.web.pages.users.FormPanel;

public class EditProposalPanel extends FormPanel
{
  final Model<String> modelTitle = new Model<String>();
  final Model<String> modelAuthor = new Model<String>();
  Model<String> modelDesc = new Model<String>();
  Model<UserModel> modelUser = new Model<UserModel>();

  @SpringBean
  UiFacade facade;


  private ProposalModel _talk;

  public EditProposalPanel(String id, ProposalModel talk)
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
    modelAuthor.setObject(_talk.getAuthor());
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
        setResponsePage(ProposalOverviewPage.class);
      }
    };
    cancel.setDefaultFormProcessing(false);
    form.add(cancel);

    FormComponent<String> titleField = new TextField<String>("title", modelTitle);
    TextField<String> authorfield = new TextField<String>("author", modelAuthor);
    titleField.setRequired(true);
    titleField.add(new MaximumLengthValidator(254));
    titleField.add(new InputHintBehavior(form, "Kurz und pr\u00e4gnant", "color: #aaa;"));
    authorfield.add(new MaximumLengthValidator(254));
    authorfield.add(new InputHintBehavior(form, "Der/Die Vortragende(en)", "color: #aaa;"));
    
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
        facade.deleteProposal(_talk);
        setResponsePage(ProposalOverviewPage.class);
      }
    };
    if (_talk.isNew() || _talk.getConference()!=null)
    {
      btnDel.setVisible(false);
    }
    
    form.add(titleField);
    form.add(authorfield);
    form.add(new RichTextEditorFormBehavior());
    ReducedRichTextEditor rte = new ReducedRichTextEditor("description", modelDesc);
    form.add(rte);
    form.add(ddc);
    form.add(btnDel);
    form.add(new Button("submit"));
    add(form);
  }


  protected void clickedOnSave()
  {
    String valueTitle = modelTitle.getObject();
    String valueAuthor = modelAuthor.getObject();
    String valueDescRaw = modelDesc.getObject();
    String valueDesc = HtmlSanitizer.sanitize(valueDescRaw);
    UserModel valueUser = modelUser.getObject();
    _talk.setOwner(valueUser);
    _talk.setTitle(valueTitle);
    _talk.setAuthor(valueAuthor);
    _talk.setDescription(valueDesc);
    facade.saveProposal(_talk);
    setResponsePage(ProposalOverviewPage.class);
  }

}
