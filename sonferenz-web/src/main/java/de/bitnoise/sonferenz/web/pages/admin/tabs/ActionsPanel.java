package de.bitnoise.sonferenz.web.pages.admin.tabs;

import de.bitnoise.sonferenz.web.component.panels.KonferenzTabPanel;

public class ActionsPanel extends KonferenzTabPanel
{
  public ActionsPanel(String id)
  {
    super(id);
  }

  @Override
  protected void onInitialize()
  {
    super.onInitialize();
    add(new SubListTalksPanel("listTalks"));
  }
}
