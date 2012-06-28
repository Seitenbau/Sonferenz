package de.bitnoise.sonferenz.web.pages.play;

import java.util.List;

import javax.servlet.ServletContext;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.StringHeaderContributor;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.bitnoise.sonferenz.model.ResourceModel;
import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.service.v2.services.TalkService;

public class PlayVideoPanel extends Panel
{
  long _talkId;
  
  @SpringBean
  TalkService talks;

  public PlayVideoPanel(String id, long talkId)
  {
    super(id);
    _talkId = talkId;
  }

  @Override
  protected void onInitialize()
  {
    super.onInitialize();
    
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
    final String name = video.getOriginalName();
    
    ServletContext servletContext = WebApplication.get().getServletContext();
    String contextPath = servletContext.getContextPath(); 
    
    add(new StringHeaderContributor(
    		"<script src=\"" + contextPath + "/projekktor/projekktor-1.0.22r82.min.js\"></script>"));
    add(new StringHeaderContributor(
        "<link rel=\"stylesheet\" href=\"" + contextPath + "/projekktor/theme/style.css\" type=\"text/css\" media=\"screen\" />"));
    
    WebMarkupContainer v = new WebMarkupContainer("video"){
      @Override
      protected void onComponentTag(ComponentTag tag)
      {
//        tag.addBehavior(new AttributeModifier("poster", Model.of(name)));
      }
    };
//    WebMarkupContainer s = new WebMarkupContainer("source"){
//      @Override
//      protected void onComponentTag(ComponentTag tag)
//      {
////        tag.put("src", name);
//      }
//    };
//    v.add(s);
    add(v);
    add(new Label("videoFile",name));
    add(new Label("videoThumb",name+".jpg"));
  }
}
