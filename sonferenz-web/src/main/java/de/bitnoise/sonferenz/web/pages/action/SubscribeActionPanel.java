package de.bitnoise.sonferenz.web.pages.action;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.PatternValidator;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.service.v2.actions.Aktion;
import de.bitnoise.sonferenz.service.v2.actions.impl.SubscribeActionImpl.ActionCreateUser;
import de.bitnoise.sonferenz.service.v2.exceptions.UserExistsException;
import de.bitnoise.sonferenz.service.v2.exceptions.ValidationException;
import de.bitnoise.sonferenz.web.app.KonferenzDefines;
import de.bitnoise.sonferenz.web.pages.statics.ConferencePage;
import de.bitnoise.sonferenz.web.pages.users.FormPanel;

public class SubscribeActionPanel extends FormPanel
{

  @SpringBean
  UiFacade facade;

  ActionCreateUser _data;

  TextField<String> display;

  TextField<String> login;

  PasswordTextField password1;

  PasswordTextField password2;

  TextField<String> email1;

//  TextField<String> email2;

  public SubscribeActionPanel(String id, Aktion aktion)
  {
    super(id);
    _data = (ActionCreateUser) aktion.getData();
    if (_data == null)
    {
      _data = new ActionCreateUser();
    }
    _data.setTokenId(aktion.getId());
  }

  @Override
  protected void onInitialize()
  {
    super.onInitialize();
    addFeedback(this, "feedback");
    display = new TextField<String>("display", Model.of(""));
    login = new TextField("login", Model.of(""));
    email1 = new TextField("mail1", Model.of(""));
//    email2 = new TextField("mail2", Model.of(""));
    
    /* in case of an invite, the email & username already exist.*/
    if (_data.getEMail() != null)
    {
    	email1.setModelValue(new String[]{_data.getEMail()});
    	email1.setEnabled(false);
//    	email2.setModelValue(_data.getEMail());
//    	email2.setEnabled(false);
    }
    if (_data.getLoginName() != null)
    {
    	display.setModelValue(new String[]{_data.getLoginName()});
    	login.setModelValue(new String[]{_data.getLoginName()});
    }
    //login.setEnabled(true);
    
    password1 = new PasswordTextField("password1", Model.of(""));
    password2 = new PasswordTextField("password2", Model.of(""));

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

    // add validators
    login.setRequired(true);
    login.add(new PatternValidator(KonferenzDefines.REGEX_USERNAME));
    login.add(new UserUnique());
    display.add(new UserUnique());
    display.setRequired(true);
    display.add(new PatternValidator(KonferenzDefines.REGEX_USER_DISPLAY));
    password1.add(new PatternValidator(KonferenzDefines.PASSWORD_REGEX));
    email1.add(EmailAddressValidator.getInstance());
    email1.add(new EMailNotUsed());
    form.add(new EqualPasswordInputValidator(password1, password2));
//    form.add(new EqualInputValidator(email1, email2));

    // add components
    add(form);
    form.add(display);
    form.add(login);
    form.add(password1);
    form.add(password2);
    form.add(email1);
//    form.add(email2);
    form.add(new Button("submit"));
    form.add(cancel);
  }
  
  public class UserUnique<String> extends AbstractValidator<String>
  {
    @Override
    protected void onValidate(IValidatable<String> validatable)
    {
      java.lang.String x = (java.lang.String) validatable.getValue();
      if (!facade.checkUserNotExists(x))
      {
        error(validatable);
      }
    }

    @Override
    public java.lang.String toString() {
      return "Username schon vergeben";
    }
  }

  public class EMailNotUsed extends AbstractValidator<String>
  {

    @Override
    protected void onValidate(IValidatable<String> validatable)
    {
      String mail = validatable.getValue();
      if (!facade.checkMailNotExists(mail))
      {
        error(validatable);
      }
    }
  }

  protected void onSubmitForm()
  {
    try
    {
      _data.setLoginName(login.getValue());
      _data.setUserName(display.getValue());
      _data.setPassword(password1.getValue());
      //_data.setProvider(CrowdIdp.IDP_NAME);
      if (_data.getEMail() != null)
      {
    	  /* in case of an invite, the email already exist.*/
    	  _data.setMail(email1.getValue());
      }
      facade.executeAction(_data);
      setResponsePage(ActionSuccessPage.class);
    }
    catch (ValidationException ve)
    {
      error(ve.getMessage());
    }
    catch (UserExistsException e)
    {
    	error(e.getMessage());
    }
  }

}
