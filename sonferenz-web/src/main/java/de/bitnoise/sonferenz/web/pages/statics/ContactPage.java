package de.bitnoise.sonferenz.web.pages.statics;

import org.apache.wicket.markup.html.panel.Panel;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.web.pages.KonferenzPage;

@At(url = "/contact")
public class ContactPage extends KonferenzPage
{
  @Override
  protected Panel getPageContent(String id)
  {
      return new ContactPanel(id);
  }
  
}
