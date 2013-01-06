package de.bitnoise.sonferenz.web.pages.proposal;

import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.ProposalModel;
import de.bitnoise.sonferenz.service.v2.services.StaticContentService;
import de.bitnoise.sonferenz.web.component.SortableServiceDataProvider;
import de.bitnoise.sonferenz.web.component.TableBuilder;
import de.bitnoise.sonferenz.web.component.link.AjaxLink;
import de.bitnoise.sonferenz.web.pages.proposal.action.EditOrViewProposal;

public class ListProposalPanel extends Panel
{
  

  @SpringBean
  StaticContentService content2;
  
  @SpringBean 
  UiFacade facade;
  
  public ListProposalPanel(String id)
  {
    super(id);
    InjectorHolder.getInjector().inject(this);
  }

  @Override
  protected void onInitialize()
  {
    super.onInitialize();
    final Set<ModelProposalList> selected = new HashSet<ModelProposalList>();
    TableBuilder<ModelProposalList> builder = new TableBuilder<ModelProposalList>(
        "proposals")
    {
      {
        addColumn(new Column()
        {
          {
            setTitle("Titel");
            setModelProperty("title");
            sortable();
            action(new EditOrViewProposal());
          }
        });
//        addColumn(new Column()
//        {
//          {
//            setTitle("Description");
//            setEscaping(false);
//            setModelProperty("description");
//          }
//        });
        addColumn(new Column()
        {
          {
            setTitle("Author");
            setModelProperty("author");
            sortable();
          }
        });
        // addColumn(new Column() {
        // {
        // setTitle("Owner");
        // setModelProperty("owner");
        // sortable();
        // }
        // });
      }
    };

    ISortableDataProvider<ModelProposalList,SortParam<String>> provider = new SortableServiceDataProvider<ProposalModel, ModelProposalList>( )
    {
      @Override
      protected ModelProposalList transferType(ProposalModel dbObject)
      {
        ModelProposalList item = new ModelProposalList();
        item.author = dbObject.getAuthor();
        item.title = dbObject.getTitle();
        item.owner = dbObject.getOwner().toString();
        item.description = createShortDescription(dbObject);
        item.talk = dbObject;
        return item;
      }

      @Override
      protected Page<ProposalModel> getAllItems(PageRequest request)
      {
        return facade.getProposals(request);
      }

      @Override
      public long size()
      {
        return facade.getAllProposalsCount();
      }

      @Override
      protected Sort createDefaultSorting()
      {
        return new Sort(new Order(Direction.ASC, "title"));
      }

    };
    String text=content2.text("page.proposalsHeader","");
    add(new Label("headerText",text).setEscapeModelStrings(false));
    DefaultDataTable<ModelProposalList,SortParam<String>> table = new DefaultDataTable<ModelProposalList,SortParam<String>>(
        "talkTable", builder.getColumns(), provider, 100);
    
    add(new AjaxLink("above","table.proposals.create") {
		@Override
		protected void onClickLink(AjaxRequestTarget target) {
			ProposalOverviewPage page = new ProposalOverviewPage();
		    page.createNew();
			setResponsePage(page );
		}
	});
    
//    AddToolbarWithButton toolbar = new AddToolbarWithButton("+ create Talk",
//            table, new CreateTalk());
//        table.addBottomToolbar(toolbar);
    add(table);
  }

  private String createShortDescription(ProposalModel dbObject)
  {
    String desc = dbObject.getDescription();

    return creatShort(desc);
  }

  public static String creatShort(String desc)
  {
    if (desc == null)
    {
      return null;
    }
    int start = first(desc, "<br", desc.length());
    // start = first(desc, "<span", start);
    start = first(desc, "<ol", start);
    start = first(desc, "<ul", start);
    if (start > 0)
    {
      desc = desc.substring(0, start);
    }
    if (desc.length() > 256)
    {
      desc = desc.substring(0, 256);
    }
    return desc;
  }

  private static int first(String desc, String string, int start)
  {
    int current = desc.indexOf(string);
    if (current != -1)
    {
      return Math.min(current, start);
    }
    else
    {
      return start;
    }
  }
}
