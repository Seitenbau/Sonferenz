package de.bitnoise.sonferenz.web.pages.auth;

import org.apache.wicket.model.IModel;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.web.action.IWebAction;
import de.bitnoise.sonferenz.web.pages.statics.ConferencePage;

@At(url = "/login")
public class LoginPage extends ConferencePage
{

	public LoginPage(IWebAction iWebAction, IModel<Object> model) {
		super();
    }

	public LoginPage() {
    }
 
}
