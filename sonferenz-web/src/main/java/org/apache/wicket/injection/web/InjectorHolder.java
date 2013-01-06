package org.apache.wicket.injection.web;

import org.apache.wicket.injection.Injector;

public class InjectorHolder {

	public static Injector getInjector() {
		return Injector.get();
	}

}
