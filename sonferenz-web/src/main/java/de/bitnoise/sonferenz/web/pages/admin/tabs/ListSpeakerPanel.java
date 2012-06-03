package de.bitnoise.sonferenz.web.pages.admin.tabs;

import java.io.Serializable;
import java.util.Set;
import static de.bitnoise.sonferenz.web.pages.KonferenzPage.txt;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.AuthMapping;
import de.bitnoise.sonferenz.model.SpeakerModel;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.model.UserRole;
import de.bitnoise.sonferenz.service.v2.services.SpeakerService;
import de.bitnoise.sonferenz.web.action.WebAction;
import de.bitnoise.sonferenz.web.component.SortableServiceDataProvider;
import de.bitnoise.sonferenz.web.component.TableBuilder;
import de.bitnoise.sonferenz.web.component.link.AjaxLink;
import de.bitnoise.sonferenz.web.component.panels.KonferenzTabPanel;
import de.bitnoise.sonferenz.web.pages.admin.actions.CreateNewUser;
import de.bitnoise.sonferenz.web.pages.admin.actions.EditUser;
import de.bitnoise.sonferenz.web.pages.admin.model.UserListItem;
import de.bitnoise.sonferenz.web.pages.speakers.PageSpeaker;
import de.bitnoise.sonferenz.web.pages.speakers.PageSpeakers;
import de.bitnoise.sonferenz.web.pages.users.UserOverviewPage;
import de.bitnoise.sonferenz.web.toolbar.AddToolbarWithButton;

public class ListSpeakerPanel extends KonferenzTabPanel
{

  @SpringBean
  SpeakerService speakers;

  public ListSpeakerPanel(String id)
  {
    super(id);
    InjectorHolder.getInjector().inject(this);
  }

  @Override
  protected void onInitialize()
  {
    super.onInitialize();

    TableBuilder<SpeakerListItem> builder = new TableBuilder<SpeakerListItem>("speakers")
    {
      {
        addColumn(new Column()
        {
          {
            setTitle("Name");
            setModelProperty("name");
            sortable();
            action(new EditSpeaker());
          }
        });
      }
    };

    SortableServiceDataProvider<SpeakerModel, SpeakerListItem> provider = createProvider();
    DefaultDataTable<SpeakerListItem> table = new DefaultDataTable<SpeakerListItem>(
        "speakerTable", builder.getColumns(), provider, 100);

    add(new AjaxLink("above", "table.speakers.createLink") {
      @Override
      protected void onClickLink(AjaxRequestTarget target) {
        setResponsePage(new PageSpeaker((SpeakerModel) null));
      }
    });
    add(table);
  }

  private SortableServiceDataProvider<SpeakerModel, SpeakerListItem> createProvider()
  {
    SortableServiceDataProvider<SpeakerModel, SpeakerListItem> provider = new SortableServiceDataProvider<SpeakerModel, SpeakerListItem>()
    {
      @Override
      protected SpeakerListItem transferType(SpeakerModel dbObject)
      {
        SpeakerListItem user = new SpeakerListItem();
        user.name = dbObject.getName();
        user.speaker = dbObject;
        return user;
      }

      @Override
      protected Page<SpeakerModel> getAllItems(PageRequest request)
      {
        return speakers.getAllSpeakers(request);
      }

      @Override
      protected Sort createDefaultSorting()
      {
        return new Sort(Direction.ASC, "name");
      }

    };
    return provider;
  }

  public class SpeakerListItem implements Serializable
  {
    public String name;
    public SpeakerModel speaker;
  }

  public class EditSpeaker extends WebAction<IModel<SpeakerListItem>>
  {

    public org.apache.wicket.Page doAction(IModel<SpeakerListItem> model)
    {
      return new PageSpeaker(model.getObject().speaker);
    }

  }

}
