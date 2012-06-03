package de.bitnoise.sonferenz.web.component.state;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.model.ConferenceState;
import de.bitnoise.sonferenz.web.component.navigation.VisibleChoice;

public class OnState implements VisibleChoice
{

  @SpringBean
  transient UiFacade facade;
  
  List<ConferenceState> _any;
  
  public OnState(ConferenceState ... anyOf) {
    _any = Arrays.asList(anyOf);
  }

  public boolean canBeDisplayed()
  {
    InjectorHolder.getInjector().inject(this);
    ConferenceModel active = facade.getActiveConference();
    if (active == null)
    {
      return false;
    }
    return _any.contains( active.getState() );
  }

}
