package de.bitnoise.sonferenz.web.pages.talks;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;

import de.bitnoise.sonferenz.model.ProposalModel;
import de.bitnoise.sonferenz.model.SpeakerModel;
import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.pages.speakers.ViewSpeakerPanel;
import de.bitnoise.sonferenz.web.pages.users.FormPanel;
import de.bitnoise.sonferenz.web.pages.voting.ListVotesPanel2.NumberItem;
import de.bitnoise.sonferenz.web.utils.WicketTools;

public class ViewTalkPanel extends FormPanel
{
  private TalkModel _talk;

  public ViewTalkPanel(String id, TalkModel talk)
  {
    super(id);
    InjectorHolder.getInjector().inject(this);
    _talk = talk;
  }

  @Override
  protected void onInitialize()
  {
    super.onInitialize();

    add(new Label("title", _talk.getTitle()));
    add(new Label("author", _talk.getAuthor()));
    add(new Label("owner", _talk.getOwner().getName()));
    AjaxFallbackLink<String> edit = new AjaxFallbackLink<String>("editTalk") {
      @Override
      public void onClick(AjaxRequestTarget target) {
        TalksOverviewPage userOverviewPage = new TalksOverviewPage();
        userOverviewPage.editTalk(_talk);
        setResponsePage(userOverviewPage);
      }
    };
    edit.setVisibilityAllowed(false);
    if (KonferenzSession.isUser(_talk.getOwner()) || KonferenzSession.isAdmin()) {
      edit.setVisibilityAllowed(true);
    }
    add(edit);
    Label desc = new Label("description", _talk.getDescription());
    desc.setEscapeModelStrings(false);
    
    add(newSpeakerList("items",_talk.getSpeakers()));
    add(desc);
    
  }

  private Component newSpeakerList(String id, List<SpeakerModel> speakers) {
    ListView<SpeakerModel> numbers = new ListView<SpeakerModel>(id, speakers) {
      @Override
      protected void populateItem(ListItem<SpeakerModel> item) {
        SpeakerModel object = item.getModel().getObject();
        item.add(new ViewSpeakerPanel("speaker", object));
      }
    };
    return numbers;
  }

}
