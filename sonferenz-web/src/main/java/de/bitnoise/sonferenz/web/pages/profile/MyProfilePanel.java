package de.bitnoise.sonferenz.web.pages.profile;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.apache.wicket.validation.validator.PatternValidator;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.web.app.KonferenzDefines;
import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.app.Right;
import de.bitnoise.sonferenz.web.pages.statics.ConferencePage;
import de.bitnoise.sonferenz.web.pages.users.FormPanel;

public class MyProfilePanel extends FormPanel
{
	@SpringBean
	UiFacade facade;
	
	TextField display;
	UserModel user;
	PasswordTextField pwd1;
	PasswordTextField pwd2;

	String _old;
	String _login;

	IModel<String> modelPwd1=Model.of("");

	IModel<String> modelPwd2=Model.of("");

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
		_old = user.getName();
		_login = user.getProvider().getAuthId();
		display = new TextField<String>("display", Model.of(_old));

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
		display.add(new UserUnique());
		display.add(new PatternValidator(KonferenzDefines.REGEX_USER_DISPLAY));
		display.setRequired(true);
		add(form);
		form.add(display);
		TextField email = new TextField("email", Model.of(user.getEmail()));
		email.setEnabled(false);
		TextField login = new TextField("login", Model.of(user.getProvider().getAuthId()));
		login.setEnabled(false);
		pwd1=new PasswordTextField("password1",modelPwd1);
		pwd2=new PasswordTextField("password2",modelPwd2);
		form.add(pwd1);
		form.add(pwd2);
		pwd1.add(new PatternValidator(KonferenzDefines.PASSWORD_REGEX));
		pwd1.setRequired(false);
		pwd2.setRequired(false);
		form.add(new EqualPasswordInputValidator(pwd1,pwd2));
		
//		if(!KonferenzSession.hasRight(Right.User.Edit)) 
//		{
//			pwd1.setEnabled(false);
//			pwd2.setEnabled(false);
//		}
		form.add(login);
		form.add(email);
		form.add(new Button("submit"));
		form.add(cancel);
	}

	protected void onSubmitForm()
	{
		setResponsePage(MyProfilePage.class);
		String newName = display.getValue();
		if(!pwd1.getValue().isEmpty()) 
		{
			facade.requestPasswordChange(user.getProvider(), pwd1.getValue());
		}
		if (user.getName().equals(newName))
		{
			return;
		}
		facade.userUpdate(user, newName);
	}

	public class UserUnique extends AbstractValidator<String>
	  {
	    @Override
	    protected void onValidate(IValidatable<String> validatable)
	    {
	      java.lang.String neu = (java.lang.String) validatable.getValue();
	      if ( _old.equals(neu) ) {
	    	  return;
	      }
	      if( neu.equals(_login)) {
	    	  return;
	      }
	      if(!facade.checkUserNotExists(neu))
	      {
	        error(validatable);
	      }
	    }

	    @Override
	    public java.lang.String toString() {
	      return "Username schon vergeben";
	    }
	  }
}
