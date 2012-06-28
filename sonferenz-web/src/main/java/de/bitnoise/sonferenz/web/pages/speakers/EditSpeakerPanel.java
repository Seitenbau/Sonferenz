package de.bitnoise.sonferenz.web.pages.speakers;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.validation.validator.StringValidator.MaximumLengthValidator;
import org.springframework.beans.factory.annotation.Autowired;

import com.visural.common.web.HtmlSanitizer;
import com.visural.wicket.behavior.inputhint.InputHintBehavior;
import com.visural.wicket.component.nicedit.RichTextEditorFormBehavior;

import de.bitnoise.sonferenz.model.FileResourceModel;
import de.bitnoise.sonferenz.model.SpeakerModel;
import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.service.v2.services.SpeakerService;
import de.bitnoise.sonferenz.service.v2.services.UserService;
import de.bitnoise.sonferenz.service.v2.services.impl.ResourceService;
import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.app.Right;
import de.bitnoise.sonferenz.web.app.WicketApplication;
import de.bitnoise.sonferenz.web.component.rte.ReducedRichTextEditor;
import de.bitnoise.sonferenz.web.pages.admin.AdminPage;
import de.bitnoise.sonferenz.web.pages.profile.MyProfilePage;
import de.bitnoise.sonferenz.web.pages.resources.PageImages;
import de.bitnoise.sonferenz.web.pages.talks.TalksOverviewPage;
import de.bitnoise.sonferenz.web.pages.users.FormPanel;

public class EditSpeakerPanel extends FormPanel {
  final Model<String> modelName = new Model<String>();
  Model<String> modelDesc = new Model<String>();
  Model<UserModel> modelUser = new Model<UserModel>();

  SpeakerModel _speaker;

  @SpringBean
  UserService userService;

  @SpringBean
  SpeakerService speakerService;

  @SpringBean
  ResourceService resources;

  public EditSpeakerPanel(String id, SpeakerModel speaker)
  {
    super(id);
    _speaker = speaker;
    if (_speaker == null) {
      _speaker = new SpeakerModel();
    }
    if (!KonferenzSession.hasRight(Right.Actions.ManageInviteUser)) {
      throw new IllegalStateException("You have to less rights");
    }
  }

  final ResourceReference img0 = new ResourceReference(WicketApplication.class,"images/nopic.gif");

  @Override
  protected void onInitialize()
  {
    super.onInitialize();

    // Prepare model
    modelName.setObject(_speaker.getName());
    modelDesc.setObject(_speaker.getDescription());
    modelUser.setObject(_speaker.getContact());
    Image profileImage = new Image("profileImage", img0);
    if( _speaker.getPicture() != null ) {
      profileImage = new Image("profileImage", new PageImages(_speaker.getPicture()) );
    }
    // Bind Wicket Elements
    addFeedback(this, "feedback");
    final FileUploadField fileupload = new FileUploadField("bild");
    Form<String> form = new Form<String>("form") {
      @Override
      protected void onSubmit()
      {
        clickedOnSave(fileupload);
      }
    };

    Button cancel = new Button("cancel") {
      public void onSubmit()
      {
        setResponsePage(TalksOverviewPage.class);
      }
    };

    cancel.setDefaultFormProcessing(false);
    form.setMultiPart(true);
    form.setMaxSize(Bytes.kilobytes(124));
    form.add(new UploadProgressBar("progressbar", form));
    form.add(fileupload);
    form.add(profileImage);
    form.add(cancel);

    FormComponent<String> titleField = new TextField<String>("name", modelName);
    titleField.setRequired(true);
    titleField.add(new MaximumLengthValidator(254));
    titleField.add(new InputHintBehavior(form, "Kurz und pr\u00e4gnant", "color: #aaa;"));

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
    DropDownChoice<UserModel> ddc = new DropDownChoice<UserModel>("user",
        modelUser, choices, render);
    ddc.setRequired(true);

    form.add(titleField);
    form.add(new RichTextEditorFormBehavior());
    ReducedRichTextEditor rte = new ReducedRichTextEditor("description", modelDesc);
    form.add(rte);
    form.add(ddc);
    form.add(new Button("submit"));
    add(form);
  }

  protected void clickedOnSave(FileUploadField fileupload) {
    if (!KonferenzSession.hasRight(Right.Actions.ManageInviteUser)) {
      throw new IllegalStateException("You have to less rights");
    }
    String valueName = modelName.getObject();
    String valueDescRaw = modelDesc.getObject();
    String valueDesc = HtmlSanitizer.sanitize(valueDescRaw);
    FileUpload fup = fileupload.getFileUpload();

    if (fup != null) {
      byte[] data = fup.getBytes();
      String filename = fup.getClientFileName();
      byte[] md5 = fup.getMD5();
      Long size = fup.getSize();
      FileResourceModel model = resources.storeResource(filename, data, md5, size);
      _speaker.setPicture(model);
    }

    UserModel valueUser = modelUser.getObject();

    _speaker.setContact(valueUser);
    _speaker.setName(valueName);
    _speaker.setDescription(valueDesc);
    speakerService.saveSpeaker(_speaker);
    setResponsePage(MyProfilePage.class);
  }

}
