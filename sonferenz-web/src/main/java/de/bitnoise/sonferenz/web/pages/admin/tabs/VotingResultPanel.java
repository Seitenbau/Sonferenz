package de.bitnoise.sonferenz.web.pages.admin.tabs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import lombok.Data;
import static de.bitnoise.sonferenz.web.pages.KonferenzPage.txt;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.extensions.markup.html.form.select.Select;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.AbstractRepeater;
import org.apache.wicket.model.ComponentPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.google.common.base.Objects;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.AuthMapping;
import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.model.ConferenceState;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.model.UserRole;
import de.bitnoise.sonferenz.service.v2.monitor.IMonitorState;
import de.bitnoise.sonferenz.service.v2.services.ConferenceService;
import de.bitnoise.sonferenz.service.v2.services.VoteService;
import de.bitnoise.sonferenz.service.v2.services.VotedItem;
import de.bitnoise.sonferenz.web.component.SortableServiceDataProvider;
import de.bitnoise.sonferenz.web.component.TableBuilder;
import de.bitnoise.sonferenz.web.component.drop.DropDownEnumChoice;
import de.bitnoise.sonferenz.web.component.link.AjaxLink;
import de.bitnoise.sonferenz.web.component.panels.KonferenzTabPanel;
import de.bitnoise.sonferenz.web.pages.admin.actions.CreateNewUser;
import de.bitnoise.sonferenz.web.pages.admin.actions.EditUser;
import de.bitnoise.sonferenz.web.pages.admin.model.UserListItem;
import de.bitnoise.sonferenz.web.pages.admin.tabs.VotingResultPanel.CalculateState;
import de.bitnoise.sonferenz.web.toolbar.AddToolbarWithButton;

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

    ConferenceModel conference = cservice.getActiveConference();

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
        item.add(new UsersList("talkUsers", "fragment1", item, object.getUsers()));
        item.add(new Label("talkVotes", Integer.valueOf(object.getVotes()).toString()));
      }
    };
    add(liste);

    CalculateState calcModel = new CalculateState();
    Model<CalculateState> cm = Model.of(calcModel);
    add(new CalculateForm("form", cm));
    add(new VotingResult("result", cm));
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
        }
      };
      form.add(liste);
    }
    
    protected void doSubmit() {
       List< VotedItem> items = (List<VotedItem>) _state.getObject();
       for(VotedItem item : items) {
         if(item.getChecked()) {
           System.out.print("[x] ");
         }
         System.out.println(item.getTitle());
       }
    }

    @Override
    protected void onConfigure() {
      super.onConfigure();
      _state.setObject( createModel() );
    }

    private List<VotedItem> createModel() {
      ConferenceModel conference = cservice.getActiveConference();
      List<VotedItem> state = vservice.getVoteLevel(conference);

      CalculateState cs = (CalculateState) getDefaultModelObject();
      if (cs != null && cs.getType() != null) {
        if (cs.getType().equals(CalcType.ByAbsolute)) {
          Collections.sort(state, new Comparator<VotedItem>()
          {
            @Override
            public int compare(VotedItem o1, VotedItem o2)
            {
              return o2.getAbsoluteVotes().compareTo(o1.getAbsoluteVotes());
            }
          });
        } else {
          Collections.sort(state, new Comparator<VotedItem>()
          {
            @Override
            public int compare(VotedItem o1, VotedItem o2)
            {
              return o2.getVotes().compareTo(o1.getVotes());
            }
          });
        }
        boolean check = true;
        int i = 1;
        for (VotedItem item : state) {
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

  class UsersList extends Fragment {

    List<String> users;

    public UsersList(String id, String markupId, MarkupContainer markupProvider, Set<String> users) {
      super(id, markupId, markupProvider);
      ArrayList<String> l = new ArrayList<String>(users);
      this.users = l;
    }

    @Override
    protected void onInitialize() {
      super.onInitialize();

      ListView<String> liste = new ListView<String>("userList", users) {
        @Override
        protected void populateItem(ListItem<String> item) {
          String object = item.getModel().getObject();
          item.add(new Label("name", object));
        }
      };
      add(new Label("userCount", Model.of(users.size())));
      add(liste);
    }

  }
}