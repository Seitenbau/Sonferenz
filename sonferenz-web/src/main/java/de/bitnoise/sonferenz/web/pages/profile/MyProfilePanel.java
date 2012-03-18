package de.bitnoise.sonferenz.web.pages.profile;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.bitnoise.sonferenz.KonferenzSession;
import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.statics.ConferencePage;
import de.bitnoise.sonferenz.web.pages.users.FormPanel;

public class MyProfilePanel extends FormPanel
{
	@SpringBean
	UiFacade facade;

	TextField display;

	UserModel user;

	public MyProfilePanel(String id)
	{
		super(id);
		user = KonferenzSession.get().getCurrentUser();
	}

	// public MyProfilePanel(String id, UserModel user)
	// {
	// super(id);
	// this.user = user;
	// }

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		addFeedback(this, "feedback");
		display = new TextField<String>("display", Model.of(user.getName()));

		Form<String> form = new Form<String>("form")
		{
			@Override
			protected void onSubmit()
			{
				onSubmitForm();
			}
		};
		Button cancel = new Button("cancel")
		{
			public void onSubmit()
			{
				setResponsePage(ConferencePage.class);
			}
		};
		display.setEnabled(false);
		
		add(form);
		form.add(display);
		TextField login = new TextField("login", Model.of(user.getProvider().getAuthId()));
		login.setEnabled(false);
		form.add(login);
		/* 
		form.add(new Button("submit"));
		form.add(cancel);
		*/
	}

	protected void onSubmitForm()
	{
		setResponsePage(ConferencePage.class);
		String newName = display.getValue();
		if (user.getName().equals(newName))
		{
			return;
		}
		facade.userUpdate(user, newName);
	}

}
