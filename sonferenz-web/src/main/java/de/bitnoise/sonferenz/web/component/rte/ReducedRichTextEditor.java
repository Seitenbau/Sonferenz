package de.bitnoise.sonferenz.web.component.rte;

import org.apache.wicket.model.IModel;

import com.visural.wicket.component.nicedit.RichTextEditor;

public class ReducedRichTextEditor extends RichTextEditor<IModel> {
	public ReducedRichTextEditor(String id, IModel model) {
		super(id, model);
	}

	@Override
	public boolean isButtonEnabled(
			com.visural.wicket.component.nicedit.Button button) {
		switch (button) {
		case bold:
		case italic:
		case ol:
		case ul:
		case underline:
		case strikethrough:
			return true;
		default:
			return false;
		}
	}

}