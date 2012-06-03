package de.bitnoise.sonferenz.web.pages.speakers;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.html.basic.Label;


import de.bitnoise.sonferenz.model.ProposalModel;
import de.bitnoise.sonferenz.model.SpeakerModel;
import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.web.pages.users.FormPanel;

public class ViewSpeakerPanel extends FormPanel
{
  private SpeakerModel _speaker;

  public ViewSpeakerPanel(String id, SpeakerModel talk)
  {
    super(id);
    InjectorHolder.getInjector().inject(this);
    _speaker = talk;
  }

  @Override
  protected void onInitialize()
  {
    super.onInitialize();

    add(new Label("name", _speaker.getName()));
    Label desc = new Label("description", _speaker.getDescription());
    desc.setEscapeModelStrings(false);
    add(desc);
  }

}
