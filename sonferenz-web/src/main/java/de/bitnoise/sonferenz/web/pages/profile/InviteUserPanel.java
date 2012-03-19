package de.bitnoise.sonferenz.web.pages.profile;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator.MaximumLengthValidator;

import com.visural.common.web.HtmlSanitizer;
import com.visural.wicket.behavior.inputhint.InputHintBehavior;
import com.visural.wicket.component.confirmer.ConfirmerLink;
import com.visural.wicket.component.nicedit.RichTextEditorFormBehavior;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.model.WhishModel;
import de.bitnoise.sonferenz.web.component.rte.ReducedRichTextEditor;
import de.bitnoise.sonferenz.web.pages.users.FormPanel;

public class InviteUserPanel extends FormPanel
{
	Model<String> modelUsername = new Model<String>();
	Model<String> modelEMail = new Model<String>();

	@SpringBean
	UiFacade facade;

	public InviteUserPanel(String id)
	{
		super(id);
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		// Bind Wicket Elements
		addFeedback(this, "feedback");

		Form<String> form = new Form<String>("form") {
			@Override
			protected void onSubmit()
			{
				clickedOnSave();
			}
		};

		Button cancel = new Button("cancel") {
			public void onSubmit()
			{
				setResponsePage(MyProfilePage.class);
			}
		};
		cancel.setDefaultFormProcessing(false);
		form.add(cancel);

		FormComponent<String> userField = new TextField<String>("username", modelUsername);
		userField
		        .setRequired(true)
		        .add(new MaximumLengthValidator(32))
		        .add(new InputHintBehavior(form, "Eindeutiger Benutzername", "color: #aaa;"));
		;
		FormComponent<String> emailField = new TextField<String>("email", modelEMail);
		emailField
		        .setRequired(true)
		        .add(new MaximumLengthValidator(128))
		        .add(new InputHintBehavior(form, "eMail", "color: #aaa;"));
		;

		userField.add(new UserUnique());
		emailField.add(new EMailNotUsed());
		emailField.add(EmailAddressValidator.getInstance());

		form.add(userField);
		form.add(emailField);
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

		@Override
		public java.lang.String toString() {
			return "eMail schon vergeben";
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

		@Override
		public java.lang.String toString() {
			return "Username schon vergeben";
		}
	}

	protected void clickedOnSave()
	{
		String valueUser = modelUsername.getObject();
		String valueEMail = modelEMail.getObject();
		// done
		try {
			facade.createToken(valueUser, valueEMail);
			setResponsePage(MyProfilePage.class);
		} catch (Exception e) {
			error(e.toString());
		}
	}

}