package de.bitnoise.sonferenz.web.pages.speakers;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.model.SpeakerModel;
import de.bitnoise.sonferenz.service.v2.services.SpeakerService;
import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.app.Right;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.error.UnauthorisedAccess;

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
    long id = parameters.get(PARAM_ID).toLong(-1L);
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
    if (KonferenzSession.hasRight(Right.Actions.ManageInviteUser)) {
      return new EditSpeakerPanel(id, _speaker);
    } else {
      return new ViewSpeakerPanel(id, _speaker);
    }
  }

}
