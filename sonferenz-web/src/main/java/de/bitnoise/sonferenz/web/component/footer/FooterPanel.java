package de.bitnoise.sonferenz.web.component.footer;

import org.apache.wicket.markup.html.panel.Panel;

public class FooterPanel extends Panel
{
  private static final long serialVersionUID = 100L;

  public FooterPanel(String id)
  {
    super(id);
  }
  
  @Override
  protected void onInitialize()
  {
    super.onInitialize();
    add(new VersionPanel("softwareVersion"));
  }

}
