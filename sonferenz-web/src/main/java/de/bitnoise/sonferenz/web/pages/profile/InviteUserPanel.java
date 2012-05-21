package de.bitnoise.sonferenz.web.pages.profile;

import java.util.List;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IErrorMessageSource;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator.MaximumLengthValidator;

import com.visural.wicket.behavior.inputhint.InputHintBehavior;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.service.v2.services.ConfigurationService;
import de.bitnoise.sonferenz.service.v2.services.StaticContentService;
import de.bitnoise.sonferenz.service.v2.services.idp.provider.crowd.CrowdIdp;
import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.app.Right;
import de.bitnoise.sonferenz.web.pages.users.FormPanel;

public class InviteUserPanel extends FormPanel
{
  Model<String> modelUsername = new Model<String>();

  Model<String> modelEMail = new Model<String>();

  Model<String> modelBody = new Model<String>();

  Model<String> modelSubject = new Model<String>();

  Model<String> modelProvider = new Model<String>();

  @SpringBean
  UiFacade facade;

  public InviteUserPanel(String id)
  {
    super(id);
  }

  @SpringBean
  ConfigurationService config;

  @SpringBean
  StaticContentService texte;

  @Override
  protected void onInitialize()
  {
    super.onInitialize();

    if (!config.isAvaiable("mail.create.replyTo"))
    {
      error("Missing config 'mail.create.replyTo'");
    }
    modelBody.setObject(
        texte.text("action.subscribe.mail.body", "${link}")
        );
    modelSubject.setObject(
        texte.text("action.subscribe.mail.subject", "You have been invited")
        );

    // Bind Wicket Elements
    addFeedback(this, "feedback");

    Form<String> form = new Form<String>("form")
    {
      @Override
      protected void onSubmit()
      {
        clickedOnSave();
      }
    };

    Button cancel = new Button("cancel")
    {
      public void onSubmit()
      {
        setResponsePage(MyProfilePage.class);
      }
    };
    cancel.setDefaultFormProcessing(false);
    form.add(cancel);

    FormComponent<String> textField = new TextArea<String>("mailbody",
        modelBody)
        .setRequired(true);
    FormComponent<String> subjectField = new TextField<String>("subject",
        modelSubject);
    FormComponent<String> userField = new TextField<String>("username",
        modelUsername);
    userField
        // .setRequired(true)
        .add(new MaximumLengthValidator(15))
        .add(
            new InputHintBehavior(form, "optionaler Benutzernamenvorschlag", "color: #aaa;"));
    ;
    FormComponent<String> emailField = new TextField<String>("email",
        modelEMail);
    emailField
        .setRequired(true)
        .add(new MaximumLengthValidator(128))
        .add(new InputHintBehavior(form, "eMail", "color: #aaa;"));
    ;

    List<String> providers = facade.availableProviders();
    if (config.isAvaiable("idp.default-provider"))
    {
      modelProvider.setObject(config.getStringValue("idp.default-provider"));
    }
    else
    {
      modelProvider.setObject(CrowdIdp.IDP_NAME);
    }
    FormComponent<String> providerField = new DropDownChoice<String>(
        "provider", modelProvider, providers);
    providerField.setEnabled(KonferenzSession.hasRight(Right.Admin.Configure));
    subjectField.add(new MaximumLengthValidator(200));
    subjectField.setRequired(true);
    textField.add(new ContainsToken());
    userField.add(new UserUnique());
    emailField.add(new EMailNotUsed());
    emailField.add(EmailAddressValidator.getInstance());

    form.add(userField);
    form.add(emailField);
    form.add(textField);
    form.add(subjectField);
    form.add(providerField);
    form.add(new Button("submit"));
    add(form);
  }

  public class EMailNotUsed<String> extends AbstractValidator<String>
  {
    @Override
    protected void onValidate(IValidatable<String> validatable)
    {
      java.lang.String x = (java.lang.String) validatable.getValue();
      if (!facade.checkMailNotExists(x))
      {
        error(validatable);
      }
    }

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
  }

  public static class ContainsToken<String> extends AbstractValidator<String>
  {
    @Override
    protected void onValidate(IValidatable<String> validatable)
    {
      java.lang.String data = (java.lang.String) validatable.getValue();
      if ( data == null || !data.contains("${link}"))
      {
        error(validatable);
      }
    }
  }

  protected void clickedOnSave()
  {
    String valueUser = modelUsername.getObject();
    String valueEMail = modelEMail.getObject();
    String valueBody = modelBody.getObject();
    String valueSubject = modelSubject.getObject();
    String provider = CrowdIdp.IDP_NAME;
    if (config.isAvaiable("idp.default-provider"))
    {
      provider = config.getStringValue("idp.default-provider");
    }
    String providerInput = modelProvider.getObject();
    if (providerInput != null)
    {
      provider = providerInput;
    }

    if (valueUser == null || valueUser.isEmpty())
    {
      valueUser = valueEMail;
      int at = valueEMail.indexOf("@");
      if (at != -1)
      {
        valueUser = valueEMail.substring(0, at);
      }
    }
    // done
    try
    {
      facade.createToken(valueUser, valueEMail, valueBody, valueSubject,
          provider);
      setResponsePage(MyProfilePage.class);
    }
    catch (Exception e)
    {
      error(e.toString());
    }
  }

}
