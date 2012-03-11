package de.bitnoise.sonferenz.web.component.footer;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

public class VersionPanel extends Panel
{
  private static final long serialVersionUID = 100L;

  public VersionPanel(String id)
  {
    super(id);
  }
  
  @Override
  protected void onInitialize()
  {
    super.onInitialize();
    add(new Label("versionText", de.bitnoise.sonferenz.Version.VERSION));
  }

}
