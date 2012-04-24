package de.bitnoise.sonferenz.web.pages.voting;

import java.util.List;
import java.util.Set;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;


import de.bitnoise.sonferenz.KonferenzSession;
import de.bitnoise.sonferenz.Right;
import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.model.VoteModel;
import de.bitnoise.sonferenz.web.component.SortableServiceDataProvider;
import de.bitnoise.sonferenz.web.component.TableBuilder;
import de.bitnoise.sonferenz.web.component.table.ListPanel;
import de.bitnoise.sonferenz.web.component.table.VoteColumn;
import de.bitnoise.sonferenz.web.component.table.VoteMultiStateColumn;
import de.bitnoise.sonferenz.web.pages.paper.action.EditOrViewTalk;

public class ListVotesPanel extends ListPanel<UserVoteItem>
{

  @SpringBean
  UiFacade facade;

  public ListVotesPanel(String id)
  {
    super(id);
  }

  @Override
  public boolean isVisible()
  {
    return KonferenzSession.hasRight(Right.Talk.List);
  }

  @Override
  protected List<IColumn<UserVoteItem>> createColumns()
  {
    final VoteColumn vote = new VoteColumn(new ResourceModel(
        "table.voting.column.Attending")) {
      @Override
      protected void vote(AjaxRequestTarget target,IModel<UserVoteItem> row)
      {
        vote(target,row,+1);
      }

      private void vote(AjaxRequestTarget target, IModel<UserVoteItem> row,
          int increment)
      {
        UserVoteItem object = (UserVoteItem) row.getObject();
        UserModel user = KonferenzSession.get().getCurrentUser();
        TalkModel talk = object.talk;
        boolean result = facade.vote(talk, user, increment);
        if (!result)
        {
          Integer maxVotes = talk.getConference().getVotesPerUser();
          Integer max = (maxVotes==null?6:maxVotes);
          if (talk.getConference() != null)
          {
            max = talk.getConference().getVotesPerUser();
          }
          StringResourceModel x = new StringResourceModel(
              "error.vote.maxUserVotes", row, new Object[]
                { max });
          getSession().error(x.getString());
        }
        RequestCycle.get().setResponsePage(target.getPage());
      }

      @Override
      protected void unVote(AjaxRequestTarget target,IModel<UserVoteItem> row)
      {
        vote(target,row,-1);
      }

    };

    final VoteMultiStateColumn attend = new VoteMultiStateColumn(
        new ResourceModel("table.voting.column.Attending")) {

      @Override
      protected boolean onChange(AjaxRequestTarget target, IModel rowModel,
          int next)
      {
        UserVoteItem object = (UserVoteItem) rowModel.getObject();
        UserModel user = KonferenzSession.get().getCurrentUser();
        TalkModel talk = object.talk;
        boolean result = facade.vote(talk, user, next);
        if (!result)
        {
          Integer max = 6;
          if (talk.getConference() != null)
          {
            max = talk.getConference().getVotesPerUser();
          }
          StringResourceModel x = new StringResourceModel(
              "error.vote.maxUserVotes", rowModel, new Object[]
                { max });
          getSession().error(x.getString());
        }
        RequestCycle.get().setResponsePage(target.getPage());
        return result;
      }

    };

    TableBuilder<UserVoteItem> builder = new TableBuilder<UserVoteItem>(
        "voting") {
      {
//        add(attend);
        add(vote);
        addColumn(new Column() {
          {
            setTitle("Title");
            setModelProperty("title");
            action(new EditOrViewTalk());
          }
        });
        addColumn(new Column() {
          {
            setTitle("Author");
            setModelProperty("author");
          }
        });
        // addColumn(new Column() {
        // {
        // setTitle("Rateing");
        // setModelProperty("rateing");
        // }
        // });
      }
    };
    return builder.getColumns();
  }

  @Override
  protected SortableDataProvider<UserVoteItem> createDataProviderProvider()
  {
//    VoteableTalks talks = new VoteableTalks() {
//      @Override
//      protected TalkDAO getDao()
//      {
//        return talkDao;
//      }
//    };

    SortableServiceDataProvider<TalkModel, UserVoteItem> provider = new SortableServiceDataProvider<TalkModel, UserVoteItem>() {
      @Override
      protected Page<TalkModel> getAllItems(PageRequest request)
      {
        return facade.getVotableTalks(request);
      }

      @Override
      public int size()
      {
        int s = (int) facade.getVotableTalksCount();
        return s;
      }

      @Override
      protected UserVoteItem transferType(TalkModel dbObject)
      {
        UserVoteItem item = new UserVoteItem();
        item.title = dbObject.getTitle();
        item.author = dbObject.getAuthor();
        item.talk = dbObject;
        item.id = dbObject.getId();
        VoteModel oldVote = myVote(dbObject.getVotes());
        if (oldVote != null)
        {
          item.model = oldVote;
          item.rateing = oldVote.getRateing();
        }
        // voteService.vote(dbObject, dbObject.getOwner(), 2);
        return item;
      }
      
      @Override
      protected Sort createDefaultSorting()
      {
        return new Sort(Direction.ASC,"title");
      }
    };
//    SortableServiceDataProvider2<TalkModel, UserVoteItem> provider = new SortableServiceDataProvider2<TalkModel, TalkModel>(
//        talks) {
//      @Override
//      protected UserVoteItem transferType(TalkModel dbObject)
//      {
//        UserVoteItem item = new UserVoteItem();
//        item.title = dbObject.getTitle();
//        item.author = dbObject.getAuthor();
//        item.talk = dbObject;
//        item.id = dbObject.getId();
//        VoteModel oldVote = myVote(dbObject.getVotes());
//        if (oldVote != null)
//        {
//          item.model = oldVote;
//          item.rateing = oldVote.getRateing();
//        }
//        // voteService.vote(dbObject, dbObject.getOwner(), 2);
//        return item;
//      }
//
//    };
    return provider;
  }

  protected VoteModel myVote(Set<VoteModel> votes)
  {
    UserModel currentUser = facade.getCurrentUser();
    if (votes == null || currentUser == null)
    {
      return null;
    }
    for (VoteModel vote : votes)
    {
      if (vote.getUser().equals(currentUser))
      {
        return vote;
      }
    }
    return null;
  }
}
