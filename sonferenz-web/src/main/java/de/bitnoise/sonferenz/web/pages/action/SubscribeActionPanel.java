package de.bitnoise.sonferenz.web.pages.action;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualInputValidator;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.PatternValidator;

import de.bitnoise.sonferenz.KonferenzDefines;
import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.service.v2.actions.Aktion;
import de.bitnoise.sonferenz.service.v2.actions.impl.SubscribeActionImpl.ActionCreateUser;
import de.bitnoise.sonferenz.service.v2.exceptions.ValidationException;
import de.bitnoise.sonferenz.service.v2.services.idp.IdpService;
import de.bitnoise.sonferenz.service.v2.services.idp.impl.IdpServiceImpl;
import de.bitnoise.sonferenz.service.v2.services.idp.provider.Idp;
import de.bitnoise.sonferenz.service.v2.services.idp.provider.crowd.CrowdIdp;
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

  TextField<String> email2;

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
    email2 = new TextField("mail2", Model.of(""));
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
    display.add(new PatternValidator(KonferenzDefines.REGEX_USER_DISPLAY));
    password1.add(new PatternValidator(KonferenzDefines.PASSWORD_REGEX));
    email1.add(EmailAddressValidator.getInstance());
    email1.add(new EMailNotUsed());
    form.add(new EqualPasswordInputValidator(password1, password2));
    form.add(new EqualInputValidator(email1, email2));

    // add components
    add(form);
    form.add(display);
    form.add(login);
    form.add(password1);
    form.add(password2);
    form.add(email1);
    form.add(email2);
    form.add(new Button("submit"));
    form.add(cancel);
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
      _data.setMail(email1.getValue());
      _data.setProvider(CrowdIdp.IDP_NAME);
      facade.executeAction(_data);
      setResponsePage(ActionSuccessPage.class);
    }
    catch (ValidationException ve)
    {
      error(ve.getMessage());
    }
  }

}
