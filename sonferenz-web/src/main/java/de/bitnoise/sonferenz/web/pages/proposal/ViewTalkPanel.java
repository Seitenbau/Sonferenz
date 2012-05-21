package de.bitnoise.sonferenz.web.pages.proposal;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.html.basic.Label;


import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.web.pages.users.FormPanel;

public class ViewTalkPanel extends FormPanel
{
  private TalkModel _talk;

  public ViewTalkPanel(String id, TalkModel talk)
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
    add(new Label("author", _talk.getAuthor()));
    add(new Label("owner", _talk.getOwner().getName()));
    Label desc = new Label("description", _talk.getDescription());
    desc.setEscapeModelStrings(false);
    add(desc);
  }

}
