package de.bitnoise.sonferenz.web.pages.users;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.visural.common.web.HtmlSanitizer;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.model.UserRole;
import de.bitnoise.sonferenz.model.UserRoles;
import de.bitnoise.sonferenz.service.v2.services.idp.IdpService;
import de.bitnoise.sonferenz.service.v2.services.idp.provider.Idp;
import de.bitnoise.sonferenz.web.app.KonferenzDefines;

public class EditUserPanel extends FormPanel
{
	@SpringBean
	UiFacade facade;

	@SpringBean
	IdpService idp;

	private UserModel _user;

	final Model<String> modelLogin = new Model<String>();
	final Model<String> modelName = new Model<String>();
	final Model<String> modelEMail = new Model<String>();
	final Model<String> modelPassword = new Model<String>();

	private MyModel modelRoles;

	private String _providerId;

	public EditUserPanel(String id, UserModel user)
	{
		super(id);
		_user = user;
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		InjectorHolder.getInjector().inject(this);
		modelName.setObject(_user.getName());
		modelEMail.setObject(_user.getEmail());
		modelLogin.setObject(_user.getProvider().getAuthId());
		addFeedback(this, "feedback");

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
				setResponsePage(UserOverviewPage.class);
			}
		};
		cancel.setDefaultFormProcessing(false);
		form.add(cancel);

		addTextInputField(form, "loginName", modelLogin, false)
		        .setEnabled(false);
		addTextInputField(form, "username", modelName, true);
		addTextInputField(form, "email", modelEMail, false);

		addTextInputField(form, "password", modelPassword, false)
		        .setVisibilityAllowed(idp.getProviderForUser(_user).supportsPasswordChange());
		form.add(new Button("submit"));

		List<UserRoles> current = new ArrayList<UserRoles>();
		for (UserRole role : _user.getRoles())
		{
			current.add(UserRoles.of(role));
		}
		if (current.size() == 0)
		{
			current.add(UserRoles.NONE);
		}
		modelRoles = new MyModel(current);

		List<? extends UserRoles> choices = UserRoles.asList();
		ListMultipleChoice<UserRoles> roleSelect = new ListMultipleChoice<UserRoles>(
		        "roleSelect", modelRoles, choices);
		form.add(roleSelect);

		addTextInputField(form, "created", asDate(_user.getCreatedAt()), false)
		        .setEnabled(false);
		addTextInputField(form, "lastlogin", asDate(_user.getLastLogin()), false)
		        .setEnabled(false);

		// List<IColumn<UserRolesItem>> columns = new
		// ArrayList<IColumn<UserRolesItem>>();
		// columns.add(new PropertyColumn<UserRolesItem>(Model.of("name"),
		// "name"));
		// IDataProvider<UserRolesItem> dataProvider = new
		// UserRolesProvider(_user);
		//
		// form.add(new DataTable<UserRolesItem>("roleTable", columns
		// .toArray(new IColumn[0]), dataProvider, 1024));

		add(form);

	}

	private Model<String> asDate(Date datum) {
		String dateAsString = "-";
		if (datum != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			dateAsString = sdf.format(datum);
		}
		return Model.of(dateAsString);
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
		Idp idpProvider = idp.getProviderForUser(_user);
		String valueName = HtmlSanitizer.sanitize(modelName.getObject());
		String valueEMail = modelEMail.getObject();
		if (valueEMail != null)
		{
			valueEMail = HtmlSanitizer.sanitize(valueEMail);
		}
		String pwd = modelPassword.getObject();
		if (pwd != null && idpProvider.supportsPasswordChange()) {

			if (!pwd.matches(KonferenzDefines.PASSWORD_REGEX)) {
				error("Invalid password style " + KonferenzDefines.PASSWORD_REGEX);
				return;
			}
			idpProvider.setUserPassword(_user, pwd);
		}
		Collection<UserRoles> newRoles = modelRoles.getObject();
		_user.setName(valueName);
		_user.setEmail(valueEMail);
		facade.saveUser(_user, newRoles);
		// userService.saveUser(_user, valueName, newRoles);

		setResponsePage(UserOverviewPage.class);
	}
}
