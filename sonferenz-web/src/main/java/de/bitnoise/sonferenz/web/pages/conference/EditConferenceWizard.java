package de.bitnoise.sonferenz.web.pages.conference;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.extensions.wizard.Wizard;
import org.apache.wicket.extensions.wizard.dynamic.DynamicWizardModel;
import org.apache.wicket.extensions.wizard.dynamic.DynamicWizardStep;
import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.CollectionModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.model.ConferenceState;
import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.web.component.drop.DropDownEnumChoice;
import de.bitnoise.sonferenz.web.pages.conference.table.TalkToConference;

public class EditConferenceWizard extends Wizard
{
  final Model<String> modelTitle = new Model<String>();
  final Model<String> modelVotesPerUser = new Model<String>();
  final Model<String> modelVotesRecomend = new Model<String>();
  final Model<ConferenceState> modelState = new Model<ConferenceState>();
  private IModel<Boolean> modelActive = new Model<Boolean>();
  private IncrementalList<TalkToConference> liste;
  ListModel<TalkToConference> modelTalks = new ListModel<TalkToConference>(
      liste);

  @SpringBean
  private UiFacade facade;

  private ConferenceModel _conference;

  protected class Step1 extends DynamicWizardStep
  {
    public Step1()
    {
      super(null, "Edit conference", "for Conference : "
          + _conference.getShortName());
    }

    @Override
    protected void onInitialize()
    {
      super.onInitialize();
      // Create elements
      DropDownEnumChoice<ConferenceState> dropState = new DropDownEnumChoice<ConferenceState>(
          "conferenceState", modelState) {
        {
          addEnum(ConferenceState.class);
          setRequired(true);
        }
      };
      CheckBox activeCheck = new CheckBox("conferenceActive", modelActive);

      // Add to form
      add(new FeedbackPanel("feedback").setOutputMarkupId(true));
      add(new TextField<String>("title", modelTitle).setRequired(true));
      add(new TextField<String>("votesPerUser", modelVotesPerUser)
          .setRequired(true));
      add(new TextField<String>("votesRecomend", modelVotesRecomend)
    		  .setRequired(true));
      add(dropState);
      add(activeCheck);
    }

    @Override
    public boolean isNextAvailable()
    {
      return true;
    }

    public IDynamicWizardStep next()
    {
      if (ConferenceState.VOTING.equals(modelState.getObject()))
      {
        return new Step2(this);
      }
      else
      {
        return new Step3(this);
      }
    }

    public boolean isLastStep()
    {
      return true;
    }

  }

  protected class Step2 extends DynamicWizardStep
  {
    public Step2(Step1 step1)
    {
      super(step1, "Change avaiable Talks", "");
    }

    @Override
    protected void onInitialize()
    {
      // Create elements
      List<TalkModel> all = facade.getAllTalks();
      List<TalkToConference> talks = asTalkToConference(all);
      List<TalkModel> cur = facade.getAllTalksForConference(_conference);
      liste = new IncrementalList<TalkToConference>(asTalkToConference(cur));
      modelTalks = new ListModel<TalkToConference>(liste);

      CollectionModel<TalkToConference> values = new CollectionModel<TalkToConference>(
          talks);
      IChoiceRenderer<TalkToConference> choiceRenderer = new IChoiceRenderer<TalkToConference>() {

        public Object getDisplayValue(TalkToConference object)
        {
          return object.title + " (" + object.author + ")";
        }

        public String getIdValue(TalkToConference object, int index)
        {
          return Integer.toString(object.id);
        }
      };

      Palette<TalkToConference> palette = new Palette<TalkToConference>(
          "activeTalks", modelTalks, values, choiceRenderer, 15, false);
      add(palette);
      super.onInitialize();
    }

    private List<TalkToConference> asTalkToConference(List<TalkModel> all)
    {
      List<TalkToConference> talks = new ArrayList<TalkToConference>();
      for (TalkModel talk : all)
      {
        TalkToConference item = new TalkToConference();
        item.author = talk.getAuthor();
        item.title = talk.getTitle();
        item.id = talk.getId();
        item.talk = talk;
        talks.add(item);
      }
      return talks;
    }

    public boolean isLastStep()
    {
      return true;
    }

    @Override
    public boolean isNextAvailable()
    {
      return true;
    }

    public IDynamicWizardStep next()
    {
      return new Step3(this);
    }

  }

  protected class Step3 extends DynamicWizardStep
  {
    public Step3(DynamicWizardStep prev)
    {
      super(prev, "save changes", "");
    }

    public boolean isLastStep()
    {
      return true;
    }

    public IDynamicWizardStep next()
    {
      return null;
    }
  }

  public EditConferenceWizard(String id, ConferenceModel conference)
  {
    super(id);
    InjectorHolder.getInjector().inject(this);
    _conference = conference;
    DynamicWizardModel model = new DynamicWizardModel(new Step1());
    init(model);
  }

  @Override
  protected void onInitialize()
  {
    super.onInitialize();
    // Prepare model
    modelTitle.setObject(_conference.getShortName());
    modelState.setObject(_conference.getState());
    modelActive.setObject(_conference.getActive());
    modelVotesPerUser
        .setObject(Integer.toString((_conference.getVotesPerUser() == null ? 0
            : _conference.getVotesPerUser())));
    modelVotesRecomend
    .setObject(Integer.toString((_conference.getVotesRecommend() == null ? 0
    		: _conference.getVotesRecommend())));
  }

  @Override
  public void onCancel()
  {
    setResponsePage(ConferenceOverviewPage.class);
  }

  @Override
  public void onFinish()
  {
    ConferenceState newState = modelState.getObject();
    ConferenceState oldState = _conference.getState();
    String valueTitle = modelTitle.getObject();
    Boolean active = modelActive.getObject();
    Integer votesPerUser = Integer.valueOf(modelVotesPerUser.getObject());
    Integer votesRecomend = Integer.valueOf(modelVotesRecomend.getObject());
    List<TalkToConference> data = modelTalks.getObject();
    List<TalkModel> talks = new ArrayList<TalkModel>();
    if (liste != null)
    {
      facade.removeAllVotestForTalk(asTalks(liste.getRemovedItems()));
      facade.removeTalksFromConference(_conference,
          asTalks(liste.getRemovedItems()));
      facade.addTalksToConference(_conference, asTalks(liste.getAllItems()));
    }
    _conference.setState(newState);
    _conference.setShortName(valueTitle);
    _conference.setActive(active);
    _conference.setVotesPerUser(votesPerUser);
    _conference.setVotesRecommend(votesRecomend);
    facade.storeConference(_conference);

    doStateActions(_conference,oldState, newState);
    setResponsePage(ConferenceOverviewPage.class);
  }

  protected void doStateActions(ConferenceModel conference, ConferenceState oldState, ConferenceState newState)
  {
    if (oldState == newState)
    {
      return;
    }
    switch (newState)
    {
    case FINISHED:
      doFinishAction(conference);
      break;
    default:
      break;
    }
  }

  private void doFinishAction(ConferenceModel conference)
  {
    
  }

  private List<TalkModel> asTalks(List<TalkToConference> rawList)
  {
    List<TalkModel> result = new ArrayList<TalkModel>();
    if (rawList == null)
    {
      return result;
    }
    for (TalkToConference data : rawList)
    {
      result.add(data.talk);
    }
    return result;
  }

}
