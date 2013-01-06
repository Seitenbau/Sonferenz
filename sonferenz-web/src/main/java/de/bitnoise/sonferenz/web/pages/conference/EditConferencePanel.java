package de.bitnoise.sonferenz.web.pages.conference;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.model.ConferenceState;
import de.bitnoise.sonferenz.web.component.drop.DropDownEnumChoice;
import de.bitnoise.sonferenz.web.pages.users.FormPanel;

public class EditConferencePanel extends FormPanel
{
  final Model<String> modelTitle = new Model<String>();
  final Model<ConferenceState> modelState = new Model<ConferenceState>();
  private IModel<Boolean> modelActive=new Model<Boolean>();

  @SpringBean
  private UiFacade facade;


  private ConferenceModel _conference;
  

  public EditConferencePanel(String id, ConferenceModel conference)
  {
    super(id);
    InjectorHolder.getInjector().inject(this);
    _conference =  conference;
  }

  @Override
  protected void onInitialize()
  {
    super.onInitialize();
    // Prepare model
    modelTitle.setObject(_conference.getShortName());
    modelState.setObject(_conference.getState());
    modelActive.setObject(_conference.getActive());
    
    // Create elements
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
        setResponsePage(ConferenceOverviewPage.class);
      }
    };
    cancel.setDefaultFormProcessing(false);
    
    DropDownEnumChoice<ConferenceState> dropState = new DropDownEnumChoice<ConferenceState>("conferenceState",modelState){{
      addEnum(ConferenceState.class);
      setRequired(true);
    }};
   CheckBox activeCheck = new CheckBox("conferenceActive",modelActive);
   
    // Add to form
   addFeedback(this, "feedback");
    addTextInputField(form, "title", modelTitle, true);
    form.add(dropState);
    form.add(activeCheck);
    form.add(cancel);
    form.add(new Button("submit"));
    add(form);
  }

  protected void clickedOnSave()
  {
    String valueTitle = modelTitle.getObject();
    ConferenceState selected = modelState.getObject();
    Boolean active = modelActive.getObject();
    _conference.setState(selected);
    _conference.setShortName(valueTitle);
    _conference.setActive(active);
    facade.storeConference(_conference);
    setResponsePage(ConferenceOverviewPage.class);
  }

}
