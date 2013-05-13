package de.bitnoise.sonferenz.web.pages.admin.tabs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import lombok.Data;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ComponentPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.model.ConferenceState;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.service.v2.services.ConferenceService;
import de.bitnoise.sonferenz.service.v2.services.VoteService;
import de.bitnoise.sonferenz.service.v2.services.VotedItem;
import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.component.drop.DropDownEnumChoice;
import de.bitnoise.sonferenz.web.component.panels.KonferenzTabPanel;
import de.bitnoise.sonferenz.web.pages.admin.AdminPage;

public class VotingResultPanel extends KonferenzTabPanel
{
  @SpringBean
  VoteService vservice;

  @SpringBean
  ConferenceService cservice;

  public VotingResultPanel(String id)
  {
    super(id);
  }

  @Override
  protected void onInitialize()
  {
    super.onInitialize();

    ConferenceModel conference = getActiveConference();

    List<VotedItem> state = vservice.getVoteLevel(conference);
    Collections.sort(state, new Comparator<VotedItem>()
    {
      @Override
      public int compare(VotedItem o1, VotedItem o2)
      {
        return o2.getVotes().compareTo(o1.getVotes());
      }
    });
    List<UserModel> users = vservice.getAllUsersNotVoted(conference);

    ListView<UserModel> listMissing = new ListView<UserModel>("missingVoteList", users) {
      @Override
      protected void populateItem(ListItem<UserModel> item) {
        UserModel object = item.getModel().getObject();
        item.add(new Label("missingVoteName", object.getName()));
      }
    };
    add(listMissing);

    ListView<VotedItem> liste = new ListView<VotedItem>("talkList", state) {
      @Override
      protected void populateItem(ListItem<VotedItem> item) {
        VotedItem object = item.getModel().getObject();
        item.add(new Label("talkName", object.getTitle()));
        item.add(new UsersList("talkUsers", object.getUsers()));
        item.add(new Label("talkVotes", Integer.valueOf(object.getVotes()).toString()));
      }
    };
    add(liste);

    CalculateState calcModel = new CalculateState();
    Model<CalculateState> cm = Model.of(calcModel);
    add(new CalculateForm("form", cm));
    add(new VotingResult("result", cm));
  }

  private ConferenceModel getActiveConference() {
    return KonferenzSession.get().getCurrentConference();
  }

  class VotingResult extends Panel {

    IModel<List<? extends VotedItem>> _state;

    public VotingResult(String id, IModel<CalculateState> model) {
      super(id, model);
    }

    @Override
    protected void onInitialize() {
      super.onInitialize();
      add(new Label("type", new ComponentPropertyModel<CalcType>("type")));
      add(new Label("counts", new ComponentPropertyModel<CalcType>("count")));
      Form<String> form = new Form<String>("form") {
        @Override
        protected void onSubmit() {
          doSubmit();
        }
      };
      add(form);
      _state=Model.ofList( createModel() );
      ListView<VotedItem> liste = new ListView<VotedItem>("talkList", _state) {
        @Override
        protected void populateItem(ListItem<VotedItem> item) {
          VotedItem object = item.getModel().getObject();
          CheckBox checkBox = new CheckBox("checked", new PropertyModel<Boolean>(object, "checked"));
          item.add(checkBox);
          checkBox.add(new Label("talkName", object.getTitle()));
          // item.add(new UsersList("talkUsers", "fragment1", item, object.getUsers()));
          checkBox.add(new Label("talkVotes", Integer.valueOf(object.getVotes()).toString()));
          checkBox.add(new Label("talkAbsolute", Integer.valueOf( object.getAbsoluteVotes() ).toString() ));
          checkBox.add(new Label("talkAuthor", object.getAuthor()));
        }
      };
      form.add(liste);
    }
    
    protected void doSubmit() {
       List< VotedItem> items = (List<VotedItem>) _state.getObject();
       ConferenceModel conference = getActiveConference();
       if(! conference.getState().equals(ConferenceState.VOTING) ) {
         error("Conference not in voting state");
         return;
       }
       for(VotedItem item : items) 
       {
         if(item.getChecked()) {
           System.out.print("[x] ");
           cservice.addTalkForProposalToConference(conference, item.getProposalId());
         }
         System.out.println(item.getTitle());
       }
       cservice.setState(conference,ConferenceState.VOTED);
       setResponsePage(AdminPage.class);
    }

    @Override
    protected void onConfigure() {
      super.onConfigure();
      _state.setObject( createModel() );
    }

    private List<VotedItem> createModel() {
      ConferenceModel conference = getActiveConference();
      List<VotedItem> state = vservice.getVoteLevel(conference);

      CalculateState cs = (CalculateState) getDefaultModelObject();
      if (cs != null && cs.getType() != null) {
        if (cs.getType().equals(CalcType.ByAbsolute)) {
          Collections.sort(state, new Comparator<VotedItem>()
          {
            @Override
            public int compare(VotedItem o1, VotedItem o2)
            {
              int val = o2.getAbsoluteVotes().compareTo(o1.getAbsoluteVotes());
              if(val!=0) {
                return val;
              }
              return o2.getVotes().compareTo(o1.getVotes());
            }
          });
        } else {
          Collections.sort(state, new Comparator<VotedItem>()
          {
            @Override
            public int compare(VotedItem o1, VotedItem o2)
            {
              int val = o2.getVotes().compareTo(o1.getVotes());
              if(val!=0) {
                return val;
              }
              return o2.getAbsoluteVotes().compareTo(o1.getAbsoluteVotes());
            }
          });
        }
        boolean check = true;
        int i = 1;
        HashMap<String, Boolean> hashMap = new HashMap<String,Boolean>();
        for (VotedItem item : state) {
          Boolean used = hashMap.get(item.getAuthor());
          if( used!=null) {
            if(check) {
              System.out.println("removed : " + item.getTitle());
            }
            continue;
          }
          hashMap.put(item.getAuthor(),true);
          item.setChecked(check);
          if (i++ >= cs.getCount()) {
            check = false;
          }
        }
      }
      return state;
    }

  }

  @Data
  class CalculateState implements Serializable {
    CalcType type;
    Integer count;
  }

  enum CalcType {
    ByAbsolute, BySumm
  }

  class CalculateForm extends Form<CalculateState> {
    IModel<Integer> _topCount = Model.of(12);
    IModel<CalcType> _choice = Model.of(CalcType.ByAbsolute);

    public CalculateForm(String id, IModel<CalculateState> calculateModel) {
      super(id, calculateModel);
    }

    @Override
    protected void onInitialize() {
      super.onInitialize();

      List<CalcType> choices = new ArrayList<CalcType>();
      choices.add(CalcType.ByAbsolute);
      choices.add(CalcType.BySumm);
      DropDownEnumChoice<CalcType> dropState = new DropDownEnumChoice<CalcType>("calculateAs", _choice) {
        {
          addEnum(CalcType.class);
          setRequired(true);
        }
      };
      add(dropState);
      add(new TextField<Integer>("topcount", _topCount, Integer.class));
    }

    @Override
    protected void onSubmit() {
      super.onSubmit();
      getModelObject().setCount(_topCount.getObject());
      getModelObject().setType(_choice.getObject());
    }

  }
}
