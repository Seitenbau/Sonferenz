package de.bitnoise.sonferenz.web.component.text;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.bitnoise.sonferenz.service.v2.services.StaticContentService;

public class StaticTextPanel extends Panel {

	String _textKey;

	@SpringBean
	StaticContentService content2;

	public StaticTextPanel(String id, String textKey) {
		super(id);
		_textKey = textKey;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		String text = content2.text(_textKey);
		Label content = new Label("content", Model.of(text));
		content.setEscapeModelStrings(false);
		add(content);
	}
}
