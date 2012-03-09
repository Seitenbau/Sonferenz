package de.bitnoise.sonferenz.web.pages.statics;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.panel.Panel;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;

@At(url = "/home")
public class ConferencePage extends KonferenzPage
{
  @Override
  protected Panel getPageContent(String id)
  {
      return new ConferencePanel(id);
  }
  
  public static PageParameters createParameters(ConferenceModel iModel)
  {
    PageParameters pp = new PageParameters();
    pp.add("conference", String.valueOf(iModel.getId()));
    return pp;
  }

}
