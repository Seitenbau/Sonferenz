package de.bitnoise.sonferenz.web.component.state;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.model.ConferenceState;
import de.bitnoise.sonferenz.web.component.navigation.VisibleChoice;

public class OnStateCallForPapers implements VisibleChoice
{

  @SpringBean
  transient UiFacade facade;

  public boolean canBeDisplayed()
  {
    InjectorHolder.getInjector().inject(this);
    ConferenceModel active = facade.getActiveConference();
    if (active == null)
    {
      return false;
    }
    return ConferenceState.CALL4PAPERS.equals(active.getState());
  }

}
