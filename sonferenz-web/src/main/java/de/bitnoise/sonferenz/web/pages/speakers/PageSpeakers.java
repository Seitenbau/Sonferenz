package de.bitnoise.sonferenz.web.pages.speakers;

import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.web.pages.KonferenzPage;

@At(url = "/speakers")
public class PageSpeakers extends KonferenzPage {

  @Override
  protected Panel getPageContent(String id)
  {
    return new EmptyPanel(id);
  }

}
