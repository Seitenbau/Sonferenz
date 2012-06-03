package de.bitnoise.sonferenz.web.pages.speakers;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.model.SpeakerModel;
import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.service.v2.services.SpeakerService;
import de.bitnoise.sonferenz.service.v2.services.TalkService;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.UnauthorizedPanel;
import de.bitnoise.sonferenz.web.pages.error.UnauthorisedAccess;
import de.bitnoise.sonferenz.web.pages.talks.action.EditOrViewTalk;

@At(url = "/speaker")
public class PageSpeaker extends KonferenzPage {

  public static final String PARAM_ID = "id";

  @SpringBean
  SpeakerService speakers;

  SpeakerModel _speaker;

  public PageSpeaker(SpeakerModel speaker) {
    _speaker = speaker;
  }

  public PageSpeaker(PageParameters parameters)
  {
    super();
    long id = parameters.getAsLong(PARAM_ID, -1L);
    if (id == -1L)
    {
      setResponsePage(UnauthorisedAccess.class);
    } 
    else
    {
      SpeakerModel speaker = speakers.getSpeakerById(id);
      if(speaker==null) {
        setResponsePage(UnauthorisedAccess.class);
      }
      _speaker = speaker;
    }
  }

  @Override
  protected Panel getPageContent(String id) {
    return new EditSpeakerPanel(id, _speaker);
  }

}
