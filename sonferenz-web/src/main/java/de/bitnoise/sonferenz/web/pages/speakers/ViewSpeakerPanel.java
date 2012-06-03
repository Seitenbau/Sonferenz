package de.bitnoise.sonferenz.web.pages.speakers;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;


import de.bitnoise.sonferenz.model.ProposalModel;
import de.bitnoise.sonferenz.model.SpeakerModel;
import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.web.app.WicketApplication;
import de.bitnoise.sonferenz.web.pages.resources.PageImages;
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
  
  final ResourceReference noImage = new ResourceReference(WicketApplication.class,"images/state-0.gif");

  @Override
  protected void onInitialize()
  {
    super.onInitialize();

    add(new Label("name", _speaker.getName()));
    Label desc = new Label("description", _speaker.getDescription());
    desc.setEscapeModelStrings(false);
    add(desc);
    
    Image profileImage = new Image("profileImage", noImage);
    if( _speaker.getPicture() != null ) {
      profileImage = new Image("profileImage", new PageImages(_speaker.getPicture()) );
    }
    add(profileImage);
  }

}
