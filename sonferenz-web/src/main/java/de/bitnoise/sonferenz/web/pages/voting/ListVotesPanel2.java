package de.bitnoise.sonferenz.web.pages.voting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import de.bitnoise.sonferenz.KonferenzSession;
import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.model.VoteModel;
import de.bitnoise.sonferenz.service.v2.services.StaticContentService;
import de.bitnoise.sonferenz.service.v2.services.VoteService;
import de.bitnoise.sonferenz.web.component.text.StaticTextPanel;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.paper.ViewTalkPage;
import de.bitnoise.sonferenz.web.pages.voting.ListVotesPanel2.NumberItem;
import de.bitnoise.sonferenz.web.utils.WicketTools;

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
	
	@SpringBean
	StaticContentService content2;

	// FIXME: MAX
	int max = 10;

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
		List<NumberItem> numberListe = buildNumberList();

		ListView<NumberItem> numbers = new ListView<NumberItem>("vote-numbering", numberListe) {
			@Override
			protected void populateItem(ListItem<NumberItem> item) {
				NumberItem object = item.getModel().getObject();
				Label label = new Label("numberItem", Model.of(object.getNumber()));
//				WebMarkupContainer div = new WebMarkupContainer("number");
				String cssClass = "should";
				if (object.getRequired()) {
					cssClass = "needed";
				}
				WicketTools.addCssClass(item, cssClass);
				item.add(label);
//				item.add(div);
			}
		};
		add(numbers);

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
				final VoteItem object = item.getModel().getObject();
				PageParameters param = new PageParameters();
			  param.add(ViewTalkPage.PARAM_ID, "" + object.getTalk().getId());
			  item.add(new AjaxFallbackLink<String>("moveup"){
          @Override
          public void onClick(AjaxRequestTarget target)
          {
            Collection<? extends Component> components = target.getComponents();
//            System.out.println(object.getTalk().getTitle());
            currentVoteListe.moveup(object);
            save.add(new SimpleAttributeModifier("class", "button savevote active"));
            target.addComponent(list);
            target.addComponent(save);
        }});
			  item.add(new AjaxFallbackLink<String>("movedown"){
			    @Override
			    public void onClick(AjaxRequestTarget target)
			    {
			      Collection<? extends Component> components = target.getComponents();
//            System.out.println(object.getTalk().getTitle());
			      currentVoteListe.movedown(object);
			      save.add(new SimpleAttributeModifier("class", "button savevote active"));
			      target.addComponent(list);
			      target.addComponent(save);
			    }});
			  BookmarkablePageLink title = new BookmarkablePageLink("link", ViewTalkPage.class, param);
				Label txt = new Label("text", Model.of(object.getTalk().getTitle()));
				item.add(txt);
				item.add(title);
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
			  save.add(new SimpleAttributeModifier("class", "button savevote active"));
				VoteItem x = transfer.getData();
				if (location==null || location.getComponent() == list)
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
		
		add(new StaticTextPanel("panelFooter","page.vote.footer"));
		add(new StaticTextPanel("description","page.vote.header"));
		
		add(new Label("headerText",content2.text("page.vote.table.header", "Wunschreihenfolge :")));
		save.add(new SimpleAttributeModifier("class", "button savevote inactive"));
		add(save);
		save.setOutputMarkupId(true);
	}

	List<NumberItem> buildNumberList() {
		List<NumberItem> result = new ArrayList<ListVotesPanel2.NumberItem>();
		ConferenceModel conf = facade.getActiveConference();
		if (conf != null) {
			Integer number = 1;
			for (int i = 0; i < conf.getVotesPerUser(); i++) {
				result.add(new NumberItem(number++ + ".", true));
			}
			for (int i = 0; i < conf.getVotesRecommend(); i++) {
				result.add(new NumberItem(number++ + ".", false));
			}
		}
		return result;
	}

	@Data
	@AllArgsConstructor
	public class NumberItem implements Serializable {
		String number;
		Boolean required;
	}

	public VoteList buildVoteList()
	{
		Map<Integer, VoteModel> votesPerTalk = getMyCurrentVotestPerTalk();
		List<TalkModel> talks = getAllTalks();
		boolean didVote=false;
		final VoteList foos = new VoteList();
		for (TalkModel talk : talks)
		{
			Integer rating = getRatingForTalk(votesPerTalk, talk);
			foos.add(new VoteItem(rating, talk));
			if(!rating.equals(Integer.MAX_VALUE)) {
			  didVote = true;
			}
		}
		if(didVote) {
  		Collections.sort(foos, new Comparator<VoteItem>()
  		{
  			@Override
  			public int compare(VoteItem o1, VoteItem o2)
  			{
  				return o2.getRateing().compareTo(o1.getRateing());
  			}
  		});
		} else {
		  // Not voted, randomize list
		  Collections.shuffle(foos);
		}
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
		int i = max;
		List<VoteModel> neueVotes = new ArrayList<VoteModel>();
		for (VoteItem f : currentVoteListe)
		{
			VoteModel vote = new VoteModel();
			vote.setRateing(i);
			if (i > 0)
			{
				i--;
			}
			vote.setTalk(f.getTalk());
			vote.setUser(user);
			neueVotes.add(vote);
		}
		votes.saveMyVotes(neueVotes);
		save.add(new SimpleAttributeModifier("class", "button savevote inactive"));
		target.addComponent(save);
	}
}
