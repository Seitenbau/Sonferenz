package de.bitnoise.sonferenz.web.pages.suggestion;

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

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.SuggestionModel;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.service.v2.services.StaticContentService;
import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.component.SortableServiceDataProvider;
import de.bitnoise.sonferenz.web.component.TableBuilder;
import de.bitnoise.sonferenz.web.component.link.AjaxLink;
import de.bitnoise.sonferenz.web.pages.proposal.ListProposalPanel;
import de.bitnoise.sonferenz.web.pages.suggestion.action.EditOrViewWhish;

public class ListWhishesPanel extends Panel
{
  @SpringBean
  StaticContentService content2;
  
  @SpringBean
  UiFacade facade;

  public ListWhishesPanel(String id)
  {
    super(id);
    InjectorHolder.getInjector().inject(this);
  }

  @Override
  protected void onInitialize()
  {
    super.onInitialize();
    setOutputMarkupId(true);
    
    TableBuilder<ModelWhishList> builder = new TableBuilder<ModelWhishList>(
        "whish") {
      {
        add(new LinkDetail("table.whish.column.Like",facade,"table.whish.hint.Like1"));
        addColumn(new Column() {
          {
            setTitle("Titel");
            setModelProperty("title");
            sortable();
            action(new EditOrViewWhish());
          }
        });
        addColumn(new Column() {
          {
            setTitle("Description");
            setEscaping(false);
            setModelProperty("description");
          }
        });
//        addColumn(new Column() {
//          {
//            setTitle("Verantwortlicher");
//            setEscaping(false);
//            setModelProperty("owner");
//            sortable();
//          }
//        });
      }
    };

    ISortableDataProvider<ModelWhishList,SortParam<String>> 
    provider = new SortableServiceDataProvider<SuggestionModel, ModelWhishList>( ) {
      @Override
      protected ModelWhishList transferType(SuggestionModel dbObject)
      {
        return load2(dbObject);
      }

      private ModelWhishList load2(SuggestionModel dbObject)
      {
        UserModel user = KonferenzSession.get().getCurrentUser();
        ModelWhishList item = new ModelWhishList();
        item.id=dbObject.getId();
        item.title = dbObject.getTitle();
        Integer val = facade.getWhishLikeCount(user, dbObject);
        item.like = val;
        item.sumLike = dbObject.getLikes();
        item.owner = dbObject.getOwner().getName();
        String desc = dbObject.getDescription();
        item.description=ListProposalPanel.creatShort(desc);
        item.whish = dbObject;
        return item;
      }

      @Override
      protected Page<SuggestionModel> getAllItems(PageRequest request)
      {
        return facade.getAllSuggestions(request);
      }

      @Override
      public long size()
      {
        return facade.getWhishesCount();
      }
      
      @Override
      protected Sort createDefaultSorting()
      {
        return new Sort(Direction.ASC,"title");
      }
    };
    
    String text=content2.text("page.whishHeader","");
    add(new Label("headerText",text).setEscapeModelStrings(false));
    
    DefaultDataTable<ModelWhishList,SortParam<String>> table = new DefaultDataTable<ModelWhishList,SortParam<String>>(
        "whishTable", builder.getColumns(), provider, 100);
    
    add(new AjaxLink("above","table.whish.create") {
		@Override
		protected void onClickLink(AjaxRequestTarget target) {
			 WhishOverviewPage page = new WhishOverviewPage();
			    page.createNew();
			setResponsePage(page );
		}
	});
    
    add(table);
  }
}
