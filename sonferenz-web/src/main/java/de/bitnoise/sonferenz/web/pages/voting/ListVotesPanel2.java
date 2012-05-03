package de.bitnoise.sonferenz.web.pages.voting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import de.bitnoise.sonferenz.KonferenzSession;
import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.model.VoteModel;
import de.bitnoise.sonferenz.service.v2.services.VoteService;

import wicketdnd.DragSource;
import wicketdnd.DropTarget;
import wicketdnd.Location;
import wicketdnd.Operation;
import wicketdnd.Reject;
import wicketdnd.Transfer;
import wicketdnd.theme.WindowsTheme;

public class ListVotesPanel2 extends Panel
{

  @SpringBean
  VoteService votes;

  @SpringBean
  UiFacade facade;

  private VoteList currentVoteListe;

  private AjaxFallbackLink<String> save;

  public ListVotesPanel2(String id)
  {
    super(id);
  }

  @Override
  protected void onInitialize()
  {
    super.onInitialize();

    add(CSSPackageResource.getHeaderContribution(new WindowsTheme()));
    final WebMarkupContainer list = new WebMarkupContainer("voting");
    currentVoteListe = buildVoteList();
    ListView<VoteItem> items = new ListView<VoteItem>("items", currentVoteListe)
    {
      @Override
      protected ListItem<VoteItem> newItem(int index)
      {
        ListItem<VoteItem> item = super.newItem(index);
        item.setOutputMarkupId(true);
        return item;
      }

      @Override
      protected void populateItem(ListItem<VoteItem> item)
      {
        VoteItem object = item.getModel().getObject();
        item.add(new Label("title", Model.of(object.getTalk().getTitle())));
      }
    };

    list.add(items);

    list.add(new DragSource(Operation.MOVE)
    {

      @Override
      public void onAfterDrop(AjaxRequestTarget target, Transfer transfer)
      {
        if (transfer.getOperation() == Operation.MOVE)
        {
          target.addComponent(list);
        }
      }
    }.drag("div.item").initiate("span.initiate"));
    
    DropTarget dropTarget = new DropTarget(Operation.MOVE)
    {
      @Override
      public void onDrop(AjaxRequestTarget target, Transfer transfer,
          Location location) throws Reject
      {
        save.setEnabled(true);
        VoteItem x = transfer.getData();
        if (location.getComponent() == list)
        {
          // foos.add(x);
        }
        else
        {
          VoteItem foo = location.getModelObject();
          switch (location.getAnchor())
          {
          case TOP:
          case LEFT:
            currentVoteListe.addBefore(x, foo);
            break;
          case BOTTOM:
          case RIGHT:
            currentVoteListe.addAfter(x, foo);
            break;
          default:
            transfer.reject();
          }

          target.addComponent(list);
          target.addComponent(save);
        }
      }
    }.dropTopAndBottom("div.item");

    list.add(dropTarget);
    add(list);
    save = new AjaxFallbackLink<String>("save",
        Model.of("Save changes"))
    {
      @Override
      public void onClick(AjaxRequestTarget target)
      {
        onSave(target);
      }
    };
    add(save);
    save.setOutputMarkupId(true);
    save.setEnabled(false);
  }

  public VoteList buildVoteList()
  {
    Map<Integer, VoteModel> votesPerTalk = getMyCurrentVotestPerTalk();
    List<TalkModel> talks = getAllTalks();

    final VoteList foos = new VoteList();
    for (TalkModel talk : talks)
    {
      Integer rating = getRatingForTalk(votesPerTalk, talk);
      foos.add(new VoteItem(rating, talk));
    }

    Collections.sort(foos, new Comparator<VoteItem>()
    {
      @Override
      public int compare(VoteItem o1, VoteItem o2)
      {
        return o1.getRateing().compareTo(o2.getRateing());
      }
    });
    return foos;
  }

  public int getRatingForTalk(Map<Integer, VoteModel> votesPerTalk,
      TalkModel talk)
  {
    VoteModel myVote = votesPerTalk.get(talk.getId());
    if (myVote == null)
    {
      return Integer.MAX_VALUE;
    }
    if (myVote.getRateing() == null)
    {
      return Integer.MAX_VALUE;
    }
    return myVote.getRateing();
  }

  public List<TalkModel> getAllTalks()
  {
    Page<TalkModel> pages = facade.getVotableTalks(new PageRequest(0, 9999));
    List<TalkModel> talks = pages.getContent();
    return talks;
  }

  public Map<Integer, VoteModel> getMyCurrentVotestPerTalk()
  {
    List<VoteModel> my = votes.getMyVotes();
    Map<Integer, VoteModel> votesPerTalk = new HashMap<Integer, VoteModel>();
    for (VoteModel v : my)
    {
      votesPerTalk.put(v.getTalk().getId(), v);
    }
    return votesPerTalk;
  }

  protected void onSave(AjaxRequestTarget target)
  {
    UserModel user = KonferenzSession.get().getCurrentUser();
    int i = 1;
    List<VoteModel> neueVotes = new ArrayList<VoteModel>();
    for (VoteItem f : currentVoteListe)
    {
      VoteModel vote = new VoteModel();
      vote.setRateing(i++);
      vote.setTalk(f.getTalk());
      vote.setUser(user);
      neueVotes.add(vote);
    }
    votes.saveMyVotes(neueVotes);
    save.setEnabled(false);
    target.addComponent(save);
  }
}
