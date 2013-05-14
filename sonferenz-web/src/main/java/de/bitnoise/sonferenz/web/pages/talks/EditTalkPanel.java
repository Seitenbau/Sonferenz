package de.bitnoise.sonferenz.web.pages.talks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.CollectionModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;
import org.springframework.data.domain.PageRequest;

import com.visural.common.web.HtmlSanitizer;
import com.visural.wicket.behavior.inputhint.InputHintBehavior;
import com.visural.wicket.component.nicedit.RichTextEditorFormBehavior;

import de.bitnoise.sonferenz.model.SpeakerModel;
import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.service.v2.services.SpeakerService;
import de.bitnoise.sonferenz.service.v2.services.TalkService;
import de.bitnoise.sonferenz.service.v2.services.UserService;
import de.bitnoise.sonferenz.web.component.rte.ReducedRichTextEditor;
import de.bitnoise.sonferenz.web.pages.users.FormPanel;

public class EditTalkPanel extends FormPanel
{
  final Model<String> modelTitle = new Model<String>();
  final Model<String> modelAuthor = new Model<String>();
  Model<String> modelDesc = new Model<String>();
  Model<UserModel> modelUser = new Model<UserModel>();

  @SpringBean
  TalkService talkService;

  @SpringBean
  UserService userService;
  
  @SpringBean
  SpeakerService speakerService;
  
  TalkModel _talk;
  
  private ListModel<SpeakerModel> modelSpeakers;

  public EditTalkPanel(String id, TalkModel talk)
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
        setResponsePage(TalksOverviewPage.class);
      }
    };
    cancel.setDefaultFormProcessing(false);
    form.add(cancel);

    FormComponent<String> titleField = new TextField<String>("title", modelTitle);
    TextField<String> authorfield = new TextField<String>("author", modelAuthor);
    titleField.setRequired(true);
    titleField.add( StringValidator.maximumLength(254));
    titleField.add(new InputHintBehavior(form, "Kurz und pr\u00e4gnant", "color: #aaa;"));
    authorfield.add(StringValidator.maximumLength(254));
    authorfield.add(new InputHintBehavior(form, "Der/Die Vortragende(en)", "color: #aaa;"));
    authorfield.setEnabled(false);

    // List<? extends UserModel> choices = userService.listAllUsers();
    LoadableDetachableModel choices = new LoadableDetachableModel() {
      @Override
      protected Object load()
      {
        return userService.getAllUsers();
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

    form.add(newSpeakerControl("speakers"));
 
    form.add(titleField);
    form.add(authorfield);
    form.add(new RichTextEditorFormBehavior());
    ReducedRichTextEditor rte = new ReducedRichTextEditor("description", modelDesc);
    form.add(rte);
    form.add(ddc);
    // form.add(btnDel);
    form.add(new Button("submit"));
    add(form);
  }

  private Palette<SpeakerModel> newSpeakerControl(String id) {
    List<SpeakerModel> allSpeakers = speakerService.getAllSpeakers(new PageRequest(0, 999)).getContent();
    modelSpeakers= new ListModel<SpeakerModel>();
    IModel<Collection<SpeakerModel>> choicesModel=new CollectionModel<SpeakerModel>(allSpeakers);
    IChoiceRenderer<SpeakerModel> choiceRenderer=new IChoiceRenderer<SpeakerModel>() {
      @Override
      public Object getDisplayValue(SpeakerModel object) {
        return ""+object.getName();
      }

      @Override
      public String getIdValue(SpeakerModel object, int index) {
        return ""+object.getId();
      }
    };
    List<SpeakerModel> current = _talk.getSpeakers();
    if(current == null ) {
      current = new ArrayList<SpeakerModel>();
    }
    modelSpeakers.setObject(current);
    Palette<SpeakerModel> sc = new Palette<SpeakerModel>(id, modelSpeakers, choicesModel, choiceRenderer, 6, false);
    return sc;
  }

  protected void clickedOnSave()
  {
    String valueTitle = modelTitle.getObject();
    String valueAuthor = modelAuthor.getObject();
    String valueDescRaw = modelDesc.getObject();
    String valueDesc = HtmlSanitizer.sanitize(valueDescRaw);
    UserModel valueUser = modelUser.getObject();
    List<SpeakerModel> speakers = modelSpeakers.getObject();
    _talk.setSpeakers(speakers);
    _talk.setOwner(valueUser);
    _talk.setTitle(valueTitle);
    _talk.setAuthor(valueAuthor);
    _talk.setDescription(valueDesc);
    talkService.saveTalk(_talk);
    
    TalksOverviewPage userOverviewPage = new TalksOverviewPage();
    userOverviewPage.viewTalk(_talk);
    setResponsePage(userOverviewPage);
  }

}
