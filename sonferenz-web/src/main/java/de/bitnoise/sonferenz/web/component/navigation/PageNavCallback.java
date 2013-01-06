/*****************************************
 * Quelltexte zum Buch: Praxisbuch Wicket
 * (http://www.hanser.de/978-3-446-41909-4)
 * 
 * Autor: Michael Mosmann (michael@mosmann.de)
 *****************************************/
package de.bitnoise.sonferenz.web.component.navigation;

import java.util.List;

import org.apache.wicket.Page;

import de.bitnoise.sonferenz.web.pages.KonferenzPage;

public class PageNavCallback extends AbstractNavCallback
{
  Class<? extends Page> _pageClass;

  private VisibleChoice[] _visibleChoice;

  public PageNavCallback(Class<? extends Page> pageClass, String name,
      VisibleChoice... c)
  {
    super(name);
    _pageClass = pageClass;
    _visibleChoice = c;
  }

  public List<NavCallbackInterface> getChilds(Page page)
  {
    if (page.getClass() == _pageClass)
    {
      if (page instanceof KonferenzPage)
      {
        return ((KonferenzPage) page).getNavigations();
      }
    }
    return null;
  }

  public boolean isActive(Page page)
  {
    return page.getClass() == _pageClass;
  }

  public void onClick(Page page)
  {
    page.setResponsePage(_pageClass);
  }

  public boolean isVisible()
  {
    if (_visibleChoice == null)
    {
      return true;
    }
    for (VisibleChoice ch : _visibleChoice)
    {
      if (!ch.canBeDisplayed())
      {
        return false;
      }
    }
    return true;
  }

}
