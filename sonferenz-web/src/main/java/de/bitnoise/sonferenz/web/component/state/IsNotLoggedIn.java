package de.bitnoise.sonferenz.web.component.state;

import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.component.navigation.VisibleChoice;

public class IsNotLoggedIn implements VisibleChoice
{
  public boolean canBeDisplayed()
  {
    return KonferenzSession.noUserLoggedIn();
  }

}
