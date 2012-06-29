package de.bitnoise.sonferenz.web.pages.play;

import javax.servlet.ServletContext;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.behavior.StringHeaderContributor;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;

import de.bitnoise.sonferenz.service.v2.services.ConfigurationService;
import de.bitnoise.sonferenz.service.v2.services.StatisticService;
import de.bitnoise.sonferenz.service.v2.services.StatisticService.ResourceEvent;

public class VideoPanel extends Panel
{
  @SpringBean
  ConfigurationService config;
  
  @SpringBean
  StatisticService stats;

  public VideoPanel(String id,String file, String title, final String sid)
  {
    super(id);
    
    ServletContext servletContext = WebApplication.get().getServletContext();
    String contextPath = servletContext.getContextPath(); 
    
    add(new StringHeaderContributor(
        "<script src=\"" + contextPath + "/projekktor/projekktor-1.0.22r82.min.js\"></script>"));
    add(new StringHeaderContributor(
        "<link rel=\"stylesheet\" href=\"" + contextPath + "/projekktor/theme/style.css\" type=\"text/css\" media=\"screen\" />"));
    
    String base = config.getStringValue("baseUrl.cdn");
    WebMarkupContainer v = new WebMarkupContainer("video");
    WebMarkupContainer s = new WebMarkupContainer("source");
    v.add(new AttributeModifier("poster",Model.of(base + file+"jpg")));
    v.add(new AttributeModifier("title",Model.of(title)));
    s.add(new AttributeModifier("src",Model.of(base + file)));
    v.add(s);
    add(v);
    
    AjaxSelfUpdatingTimerBehavior timer = new AjaxSelfUpdatingTimerBehavior(Duration.ONE_MINUTE){
      int running = 0;
      @Override
      protected void onPostProcessTarget(AjaxRequestTarget target)
      {
        running ++;
        if(running == 5) {
          stats.incrementHit(sid, ResourceEvent.ACTIVE_5MINUTES);
        } else if(running == 15) {
          stats.incrementHit(sid, ResourceEvent.ACTIVE_15MINUTES);
        } else if(running == 25) {
          stats.incrementHit(sid, ResourceEvent.ACTIVE_25MINUTES);
        } else if(running == 35) {
          stats.incrementHit(sid, ResourceEvent.ACTIVE_35MINUTES);
        } else if(running == 45) {
          stats.incrementHit(sid, ResourceEvent.ACTIVE_45MINUTES);
        } else if(running == 55) {
          stats.incrementHit(sid, ResourceEvent.ACTIVE_55MINUTES);
        } else if(running == 65) {
          stats.incrementHit(sid, ResourceEvent.ACTIVE_65MINUTES);
        }
      }
    };
    add(timer);
    
    stats.incrementHit(sid, ResourceEvent.OPEN);
  }

}