package de.bitnoise.sonferenz.web.pages.error;

import org.apache.wicket.markup.html.panel.Panel;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.web.pages.KonferenzPage;
@At(url = "/internalError")
public class InternalError extends KonferenzPage
{
  @Override
  protected Panel getPageContent(String id)
  {
    return new InernalErrorPane(id);
  }
}
