package de.bitnoise.sonferenz.web.pages.admin.tabs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import lombok.Data;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ComponentPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.model.ConferenceState;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.service.v2.services.ConferenceService;
import de.bitnoise.sonferenz.service.v2.services.VoteService;
import de.bitnoise.sonferenz.service.v2.services.VotedItem;
import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.component.drop.DropDownEnumChoice;
import de.bitnoise.sonferenz.web.component.panels.KonferenzTabPanel;
import de.bitnoise.sonferenz.web.pages.admin.AdminPage;
import de.bitnoise.sonferenz.web.pages.admin.tabs.VotingResultPanel.CalcType;

public class MailTab extends KonferenzTabPanel
{
  IModel<Integer> _topCount = Model.of(14);

  public MailTab(String id ) {
    super(id );
  }
  class ToItem implements Serializable {
    boolean checked;
    private String title;
    public ToItem(String t)
    {
      title = t;
    }
    public boolean isChecked()
    {
      return checked;
    }
    public String getTitle()
    {
      return title;
    }}
  IModel<List<? extends ToItem>> _state;
  @Override
  protected void onInitialize() {
    super.onInitialize();
    Form<String> form = new Form<String>("form") {
      @Override
      protected void onSubmit() {
        doSubmit();
      }
    };
    add(form);
    form.add(new FeedbackPanel("feedback"));
    _state=Model.ofList( createModel() );
    ListView<ToItem> liste = new ListView<ToItem>("toList", _state) {
      @Override
      protected void populateItem(ListItem<ToItem> item) {
        ToItem object = item.getModel().getObject();
        CheckBox checkBox = new CheckBox("checked", new PropertyModel<Boolean>(object, "checked"));
        item.add(checkBox);
        checkBox.add(new Label("talkName", object.getTitle()));
//        checkBox.add(new Label("talkVotes", Integer.valueOf(object.getVotes()).toString()));
//        checkBox.add(new Label("talkAbsolute", Integer.valueOf( object.getAbsoluteVotes() ).toString() ));
//        checkBox.add(new Label("talkAuthor", object.getAuthor()));
      }
    };
    form.add(liste);
//    ConferenceModel conference = KonferenzSession.get().getCurrentConference();
//    if(! conference.getState().equals(ConferenceState.VOTING) ) {
//      form.setVisibilityAllowed(false);
//    }
  }

  private List<? extends ToItem> createModel()
  {
    ArrayList<ToItem> result = new ArrayList<ToItem>();
    result.add(new ToItem("Alle Benutzer"));
    result.add(new ToItem("Alle Benutzer"));
    return result;
  }

  protected void doSubmit()
  {
    // TODO Auto-generated method stub
    
  }

   
}
