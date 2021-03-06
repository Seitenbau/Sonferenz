package de.bitnoise.sonferenz.web.pages.admin.tabs;

import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.AuthMapping;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.model.UserRole;
import de.bitnoise.sonferenz.web.component.SortableServiceDataProvider;
import de.bitnoise.sonferenz.web.component.TableBuilder;
import de.bitnoise.sonferenz.web.component.link.AjaxLink;
import de.bitnoise.sonferenz.web.component.panels.KonferenzTabPanel;
import de.bitnoise.sonferenz.web.pages.admin.actions.EditUser;
import de.bitnoise.sonferenz.web.pages.admin.model.UserListItem;
import de.bitnoise.sonferenz.web.pages.users.UserOverviewPage;

public class ListUserPanel extends KonferenzTabPanel
{
  
  @SpringBean
  UiFacade facade;

  public ListUserPanel(String id)
  {
    super(id);
    InjectorHolder.getInjector().inject(this);
  }

  @Override
  protected void onInitialize()
  {
    super.onInitialize();

    TableBuilder<UserListItem> builder = new TableBuilder<UserListItem>("users")
    {
      {
        addColumn(new Column()
        {
          {
            setTitle("Login");
            setModelProperty("login");
            action(new EditUser());
          }
        });
        addColumn(new Column()
        {
        	{
        		setTitle("Name");
        		setModelProperty("name");
        		sortable();
        		action(new EditUser());
        	}
        });
        addColumn(new Column()
        {
          {
            setTitle("Provider");
            setModelProperty("provider");
          }
        });
        addColumn(new Column()
        {
          {
            setTitle("Rollen");
            setModelProperty("roles");
          }
        });
      }
    };

    SortableServiceDataProvider<UserModel, UserListItem> provider = createProvider();
    DefaultDataTable<UserListItem,SortParam<String>> table = new DefaultDataTable<UserListItem,SortParam<String>>(
        "userTable", builder.getColumns(), provider, 100);

    add(new AjaxLink("above","table.user.createLink") {
		@Override
		protected void onClickLink(AjaxRequestTarget target) {
			UserOverviewPage page = new UserOverviewPage();
			page .createNewUser();
			setResponsePage(page );
		}
	});
    add(table);
  }

  private SortableServiceDataProvider<UserModel, UserListItem> createProvider()
  {
    SortableServiceDataProvider<UserModel, UserListItem> provider = new SortableServiceDataProvider<UserModel, UserListItem>( )
    {
      @Override
      protected UserListItem transferType(UserModel dbObject)
      {
        UserListItem user = new UserListItem();
        user.name = dbObject.getName();
        AuthMapping p = dbObject.getProvider();
        user.provider = "-";
        if (p != null)
        {
           user.login = p.getAuthId();
           user.provider = p.getAuthType();
        }
        user.roles = makeString(dbObject.getRoles());
        user.user = dbObject;
        return user;
      }

      String makeString(Set<UserRole> roles)
      {
        if (roles == null)
        {
          return "";
        }
        StringBuilder sb = new StringBuilder();
        for (UserRole role : roles)
        {
          sb.append(role.getName());
          sb.append(", ");
        }
        if (sb.length() > 2)
        {
          sb.delete(sb.length() - 2, sb.length());
        }
        return sb.toString();
      }

      @Override
      protected Page<UserModel> getAllItems(PageRequest request)
      {
        return facade.getAllUsers(request);
      }

      @Override
      public long size()
      {
        return facade.getUserCount();
      }

      @Override
      protected Sort createDefaultSorting()
      {
        return new Sort(Direction.ASC,"name");
      }
    };
    return provider;
  }
}
