package de.bitnoise.sonferenz.web.component.state;

import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.model.ConferenceState;
import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.component.navigation.VisibleChoice;

public class OnStateCallForPapers implements VisibleChoice
{
  public boolean canBeDisplayed()
  {
    ConferenceModel active = KonferenzSession.get().getCurrentConference();
    if (active == null)
    {
      return false;
    }
    return ConferenceState.CALL4PAPERS.equals(active.getState());
  }

}
