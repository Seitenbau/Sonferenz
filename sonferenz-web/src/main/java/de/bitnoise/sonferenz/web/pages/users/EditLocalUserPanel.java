package de.bitnoise.sonferenz.web.pages.users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.visural.common.web.HtmlSanitizer;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.LocalUserModel;
import de.bitnoise.sonferenz.model.UserRoles;
import de.bitnoise.sonferenz.service.v2.exceptions.UserExistsException;

public class EditLocalUserPanel extends FormPanel
{
  
  @SpringBean
  UiFacade facade;
  
  private LocalUserModel _user;

  final Model<String> modelName = new Model<String>();
  final Model<String> modelPassword1 = new Model<String>();
  final Model<String> modelPassword2 = new Model<String>();
  private MyModel modelRoles;
  private String selectedProvider;

  public EditLocalUserPanel(String id)
  {
    super(id);
    _user = new LocalUserModel();
  }

  @Override
  protected void onInitialize()
  {
    super.onInitialize();
    InjectorHolder.getInjector().inject(this);
    modelName.setObject(_user.getName());
    addFeedback(this, "feedback");

    Form<String> form = new Form<String>("form") {
      @Override
      protected void onSubmit()
      {
        onSubmitForm();
      }
    };

    Button cancel = new Button("cancel") {
      public void onSubmit()
      {
        setResponsePage(UserOverviewPage.class);
      }
    };
    cancel.setDefaultFormProcessing(false);
    form.add(cancel);

    addTextInputField(form, "username", modelName, true);
    form.add(new PasswordTextField("password1", modelPassword1));
    form.add(new PasswordTextField("password2", modelPassword2));

    form.add(new Button("submit"));

    List<UserRoles> current = new ArrayList<UserRoles>();
    current.add(UserRoles.NONE);
    modelRoles = new MyModel(current);

    List<? extends UserRoles> choices = UserRoles.asList();
    ListMultipleChoice<UserRoles> roleSelect = new ListMultipleChoice<UserRoles>(
        "roleSelect", modelRoles, choices);
    form.add(roleSelect);
    
    List<String> providers = facade.availableProviders();
    selectedProvider = providers.get(0);
    ListChoice<String> providerSelect = new ListChoice<String>(
        "providerSelect", new PropertyModel<String>(this, "selectedProvider"), providers);
    form.add(providerSelect);
    add(form);
  }

  class MyModel implements IModel<Collection<UserRoles>>
  {
    private Collection<UserRoles> _list;

    public MyModel(List<UserRoles> current)
    {
      _list = current;
    }

    public void detach()
    {
      _list = null;
    }

    public Collection<UserRoles> getObject()
    {
      return _list;
    }

    public void setObject(Collection<UserRoles> object)
    {
      _list = object;
    }
  }

  protected void onSubmitForm()
  {
    String valueName = HtmlSanitizer.sanitize(modelName.getObject());
    Collection<UserRoles> newRoles = modelRoles.getObject();
    _user.setName(valueName);
    String password = modelPassword1.getObject();
    try
    {
	    facade.createIdentity(selectedProvider, valueName, password, null, newRoles);
	    setResponsePage(UserOverviewPage.class);
    }
    catch (UserExistsException e)
    {
    	error(e.getMessage());
    }
  }
}
