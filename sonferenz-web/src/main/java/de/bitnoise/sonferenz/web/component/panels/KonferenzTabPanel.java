package de.bitnoise.sonferenz.web.component.panels;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

@SuppressWarnings("serial")
public class KonferenzTabPanel extends Panel {

  protected String _testBaseId;

  public KonferenzTabPanel(String id, String baseTextId) {
    super(id);
    _testBaseId = baseTextId;
  }

  public KonferenzTabPanel(String id) {
    super(id);
    _testBaseId = "tab." + getClass().getSimpleName();
  }

  @Override
  protected void onInitialize() {
    super.onInitialize();
    add(new AttributeModifier("id", true, Model.of(_testBaseId)));
  }

}
