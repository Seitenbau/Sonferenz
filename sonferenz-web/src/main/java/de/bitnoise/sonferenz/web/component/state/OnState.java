package de.bitnoise.sonferenz.web.component.state;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.injection.web.InjectorHolder;

import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.model.ConferenceState;
import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.component.navigation.VisibleChoice;

public class OnState implements VisibleChoice {

	List<ConferenceState> _any;

	public OnState(ConferenceState... anyOf) {
		_any = Arrays.asList(anyOf);
	}

	public boolean canBeDisplayed() {
		ConferenceModel active = KonferenzSession.get().getCurrentConference();
		if (active == null) {
			return false;
		}
		return _any.contains(active.getState());
	}

}
