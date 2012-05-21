package de.bitnoise.sonferenz.web.pages.auth;

import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;

import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.component.FocusOnLoadBehavior;
import de.bitnoise.sonferenz.web.pages.statics.InfoPage;
import de.bitnoise.sonferenz.web.pages.users.FormPanel;

public class LoginPanel extends FormPanel
{
	final Model<String> modelUsername = new Model<String>();

	final Model<String> modelPassword = new Model<String>();

	private FeedbackPanel feedback;

	public LoginPanel(String id)
	{
		super(id);
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		Form<String> form = new Form<String>("form")
		{
			@Override
			protected void onSubmit()
			{
				loginUser();
			}
		};
		form.add(new TextField<String>("username", modelUsername)
		        .add(new FocusOnLoadBehavior()));
		form.add(new PasswordTextField("password", modelPassword));
		form.add(new Button("submit"));

		feedback = new FeedbackPanel("feedback");
		feedback.setFilter(new ContainerFeedbackMessageFilter(this));
		add(feedback.setOutputMarkupId(true));
		add(form);
	}

	@Override
	protected void onConfigure() {
		super.onConfigure();

		setVisible(KonferenzSession.noUserLoggedIn());
	}

	protected void loginUser()
	{
		String username = modelUsername.getObject();
		String password = modelPassword.getObject();
		String errorMessage = KonferenzSession.get().authenticate(username,
		        password);
		if (errorMessage == null)
		{
			OnSuccessfullLogin();
		}
		else
		{
			feedback.error("Login failed : " + errorMessage);
		}
	}

	protected void OnSuccessfullLogin()
	{
		setResponsePage(InfoPage.class);
		setRedirect(true);
	}

}
