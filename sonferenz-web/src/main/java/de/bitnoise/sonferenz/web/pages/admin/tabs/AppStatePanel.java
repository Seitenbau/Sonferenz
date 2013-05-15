package de.bitnoise.sonferenz.web.pages.admin.tabs;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.wicket.Application;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.IRequestLogger;
import org.apache.wicket.protocol.http.IRequestLogger.SessionData;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.bitnoise.sonferenz.service.v2.monitor.IMonitorState;
import de.bitnoise.sonferenz.service.v2.services.MonitorService;
import de.bitnoise.sonferenz.web.component.panels.KonferenzTabPanel;

public class AppStatePanel extends KonferenzTabPanel
{

  @SpringBean
  MonitorService ms;

  public AppStatePanel(String id) {
    super(id);
  }

  @Override
  protected void onInitialize() {
    super.onInitialize();

    ListView<IMonitorState> liste = new ListView<IMonitorState>("monitorList", ms.getStates()) {
      @Override
      protected void populateItem(ListItem<IMonitorState> item) {
        IMonitorState object = item.getModel().getObject();
        item.add(new Label("monitorName", object.getMonitorName()));
        item.add(new Label("monitorStatus", object.getMonitorState().toString()));
        item.add(new Label("monitorDescription", object.getMonitorStateDetails()));
      }
    };
    add(liste);
    
    IRequestLogger requestLogger = Application.get().getRequestLogger();
    SessionData[] sessions= requestLogger.getLiveSessions(); 
    List<? extends SessionData> data = Arrays.asList(sessions);
	ListView<SessionData> session = new ListView<SessionData>("sessionList", data) {
    	@Override
    	protected void populateItem(ListItem<SessionData> item) {
    		SessionData object = item.getModel().getObject();
    		item.add(new Label("sessionId", object.getSessionId()));
    		item.add(new Label("lastActive", f(object.getLastActive())));
    		Object si = object.getSessionInfo();
    		String info="";
			if(si!=null) {
    			info = si.toString();
    		}
    		item.add(new Label("info", info));
    	
    	}
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		private String f(Date lastActive) {
			return sdf.format(lastActive);
		}
    };
    add(session);

  }

}
