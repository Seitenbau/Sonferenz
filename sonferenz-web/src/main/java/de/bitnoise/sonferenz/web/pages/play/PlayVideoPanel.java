package de.bitnoise.sonferenz.web.pages.play;

import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.bitnoise.sonferenz.model.ResourceModel;
import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.service.v2.services.ResourceService;
import de.bitnoise.sonferenz.service.v2.services.TalkService;

public class PlayVideoPanel extends Panel
{
  long _talkId;
  
  @SpringBean
  TalkService talks;
  
  @SpringBean
  ResourceService ress;
  
  public PlayVideoPanel(String id, long talkId)
  {
    super(id);
    _talkId = talkId;
    
    TalkModel talk = talks.getTalkById(_talkId);
    List<ResourceModel> resources= talk.getResources();
    ResourceModel video = null;
    for(ResourceModel res : resources) {
      if( res.getProvider().equals("video") ) {
        video = res;
      }
    }
    if(video == null) {
      throw new RuntimeException("No Video found");
    }

    ress.registerPlay(video);
    String name = video.getOriginalName();
    add(new VideoPanel("video", name,talk.getTitle(),"talk/" + talkId));
  }
}
