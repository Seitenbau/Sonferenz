package de.bitnoise.sonferenz.web.pages.reset;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.web.pages.action.ActionSuccessPage;
import de.bitnoise.sonferenz.web.pages.statics.ConferencePage;
import de.bitnoise.sonferenz.web.pages.users.FormPanel;

public class QueryUserPanel extends FormPanel {

	IModel<String> userfield = new Model<String>("");

	public QueryUserPanel(String id) {
		super(id);
	}
	
	@SpringBean
	UiFacade facade;

	private static final long serialVersionUID = 1L;

	@Override
	protected void onInitialize() {
		super.onInitialize();

		super.onInitialize();
		addFeedback(this, "feedback");

		Form<String> form = new Form<String>("form") {
			@Override
			protected void onSubmit() {
				onSubmitForm();
			}
		};
		Button cancel = new Button("cancel") {
			public void onSubmit() {
				setResponsePage(ConferencePage.class);
			}
		};

		TextField email = new TextField("email", userfield);
		
		form.add(email);
		form.add(new Button("submit"));
		form.add(cancel);
		add(form);
	}

	protected void onSubmitForm() {
		facade.queryUsername(userfield.getObject());
		setResponsePage(ActionSuccessPage.class);
	}
}
