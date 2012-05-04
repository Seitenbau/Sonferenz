package de.bitnoise.sonferenz.web.pages.users;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import static de.bitnoise.sonferenz.web.pages.KonferenzPage.txt;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.AuthMapping;
import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.model.UserRole;
import de.bitnoise.sonferenz.service.v2.services.ConferenceService;
import de.bitnoise.sonferenz.service.v2.services.VoteService;
import de.bitnoise.sonferenz.service.v2.services.VotedItem;
import de.bitnoise.sonferenz.web.component.SortableServiceDataProvider;
import de.bitnoise.sonferenz.web.component.TableBuilder;
import de.bitnoise.sonferenz.web.component.link.AjaxLink;
import de.bitnoise.sonferenz.web.pages.users.action.CreateNewUser;
import de.bitnoise.sonferenz.web.pages.users.action.EditUser;
import de.bitnoise.sonferenz.web.pages.users.table.UserListItem;
import de.bitnoise.sonferenz.web.toolbar.AddToolbarWithButton;

public class VotingResultPanel extends Panel
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
    Collections.sort(state,new Comparator<VotedItem>()
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
    sb.append("</br><ul>");
    List<UserModel> users = vservice.getAllUsersNotVoted(conference);
    for(UserModel user :users ) {
      sb.append("<li>");
      sb.append(user.getName());
    }
    sb.append("</ul>");
    
    sb.append("</br><ul>");
    for(VotedItem item : state ) {
      sb.append("<li>");
      sb.append(item.getTitle());
      sb.append(" = ");
      sb.append(item.getVotes());
    }
    sb.append("</ul>");
    
    Label content = new Label("content", Model.of(sb.toString()));
    content.setEscapeModelStrings(false);
    add(content);
  }
}
