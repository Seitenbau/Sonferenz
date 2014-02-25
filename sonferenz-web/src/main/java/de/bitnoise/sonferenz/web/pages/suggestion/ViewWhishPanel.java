package de.bitnoise.sonferenz.web.pages.suggestion;

import javax.inject.Inject;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.html.basic.Label;

import de.bitnoise.sonferenz.model.SuggestionModel;
import de.bitnoise.sonferenz.service.v2.services.ConfigurationService;
import de.bitnoise.sonferenz.web.ConfigConst;
import de.bitnoise.sonferenz.web.pages.users.FormPanel;

public class ViewWhishPanel extends FormPanel
{
  @Inject
  ConfigurationService cfg;
  
  private SuggestionModel _talk;

  public ViewWhishPanel(String id, SuggestionModel talk)
  {
    super(id);
    InjectorHolder.getInjector().inject(this);
    _talk = talk;
  }

  @Override
  protected void onInitialize()
  {
    super.onInitialize();

    add(new Label("title", _talk.getTitle()));
    Label owner = new Label("owner", _talk.getOwner().getName());
    add(owner);
    owner.setVisibilityAllowed(LikePanel.canBeDisplayed(cfg,ConfigConst.SHOW_SUGGESTION_OWNER));
    Label desc = new Label("description", _talk.getDescription());
    desc.setEscapeModelStrings(false);
    add(desc);
    
  }

}
