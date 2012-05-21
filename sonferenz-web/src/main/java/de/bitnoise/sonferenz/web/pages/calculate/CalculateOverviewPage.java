package de.bitnoise.sonferenz.web.pages.calculate;

import org.apache.wicket.markup.html.panel.Panel;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.app.Right;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.UnauthorizedPanel;

@At(url = "/calc")
public class CalculateOverviewPage extends KonferenzPage
{
  @Override
  protected Panel getPageContent(String id)
  {
    if (!KonferenzSession.hasRight(Right.Admin.ViewCalculation))
    {
      return new UnauthorizedPanel(id);
    }
    return new CalculationResultPanel(id);
  }

}
