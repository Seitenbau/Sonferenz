package de.bitnoise.sonferenz.web.pages.profile.action;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.web.action.WebAction;
import de.bitnoise.sonferenz.web.forms.KonferenzForm;
import de.bitnoise.sonferenz.web.forms.fields.StringInput;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.profile.MyProfilePage;
import de.bitnoise.sonferenz.web.pages.profile.TokenListItem;

public class TokenCreateUser extends WebAction<IModel<TokenListItem>>
{

  public Page doAction(IModel<TokenListItem> model)
  {
    return new KonferenzPage(new CreateUserToken());
  }

  class CreateUserToken extends KonferenzForm
  {
    StringInput username;
    StringInput email;
    
    @SpringBean
    UiFacade facade;

    @Override
    protected void createForm(FormBuilder builder)
    {
      builder.setLegend("Benutzer einladen");
      username = builder.addStringInputField("username");
      email = builder.addStringInputField("eMail");
      email.getInput().add(EmailAddressValidator.getInstance());
    }

    public void onSubmitForm(Component target)
    {
      String user = username.getValue();
      String mail = email.getValue();
      facade.createToken(user,mail);
      target.setResponsePage(MyProfilePage.class);
    }

    public void onCancelForm(Component target)
    {
      target.setResponsePage(MyProfilePage.class);
    }

  }
}
