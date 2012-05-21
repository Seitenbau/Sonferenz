package de.bitnoise.sonferenz.web.pages.admin.tabs;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
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

  }

}
