package de.bitnoise.sonferenz.web.pages.admin.tabs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import static de.bitnoise.sonferenz.web.pages.KonferenzPage.txt;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.AbstractRepeater;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.google.common.base.Objects;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.AuthMapping;
import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.model.UserRole;
import de.bitnoise.sonferenz.service.v2.monitor.IMonitorState;
import de.bitnoise.sonferenz.service.v2.services.ConferenceService;
import de.bitnoise.sonferenz.service.v2.services.VoteService;
import de.bitnoise.sonferenz.service.v2.services.VotedItem;
import de.bitnoise.sonferenz.web.component.SortableServiceDataProvider;
import de.bitnoise.sonferenz.web.component.TableBuilder;
import de.bitnoise.sonferenz.web.component.link.AjaxLink;
import de.bitnoise.sonferenz.web.component.panels.KonferenzTabPanel;
import de.bitnoise.sonferenz.web.pages.admin.actions.CreateNewUser;
import de.bitnoise.sonferenz.web.pages.admin.actions.EditUser;
import de.bitnoise.sonferenz.web.pages.admin.model.UserListItem;
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
    StringBuffer sb = new StringBuffer();
    sb.append("created : ");
    sb.append(new Date());
    // sb.append("</br><hr/>Noch nicht abgestimmt haben : <br/><ul>");
    List<UserModel> users = vservice.getAllUsersNotVoted(conference);
    // for(UserModel user :users ) {
    // sb.append("<li>");
    // sb.append(user.getName());
    // }
    // sb.append("</ul>");

    // sb.append("</br><hr/>Zwischenergebnis : <br/><ul>");
    // for(VotedItem item : state ) {
    // sb.append("<li>");
    // sb.append(item.getTitle());
    // sb.append(" = ");
    // sb.append(item.getVotes());
    // }
    // sb.append("</ul>");

    Label content = new Label("content", Model.of(sb.toString()));
    content.setEscapeModelStrings(false);
    add(content);

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
        item.add(new UsersList("talkUsers","fragment1", item, object.getUsers()));
        item.add(new Label("talkVotes", Integer.valueOf(object.getVotes()).toString()));
      }
    };
    add(liste);
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
          item.add(new Label("name", object ));
        }
      };
      add(liste);
    }

  }
}
