package de.bitnoise.sonferenz.web.pages.speakers;

import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.model.SpeakerModel;
import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.app.Right;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.UnauthorizedPanel;
import de.bitnoise.sonferenz.web.pages.admin.tabs.ListUserPanel;
import de.bitnoise.sonferenz.web.pages.users.EditLocalUserPanel;
import de.bitnoise.sonferenz.web.pages.users.EditUserPanel;

@At(url = "/speakers")
public class PageSpeakers extends KonferenzPage {

  @Override
  protected Panel getPageContent(String id)
  {
    return new EmptyPanel(id);
  }

}
