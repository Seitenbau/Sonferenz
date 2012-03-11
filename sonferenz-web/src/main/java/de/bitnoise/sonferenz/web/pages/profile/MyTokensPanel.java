package de.bitnoise.sonferenz.web.pages.profile;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import de.bitnoise.sonferenz.KonferenzSession;
import de.bitnoise.sonferenz.Right;
import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.ActionModel;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.web.component.SortableServiceDataProvider;
import de.bitnoise.sonferenz.web.component.TableBuilder;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.base.AbstractListPanel;
import de.bitnoise.sonferenz.web.pages.profile.action.TokenCreateUser;
import de.bitnoise.sonferenz.web.pages.statics.ConferencePage;
import de.bitnoise.sonferenz.web.toolbar.AddToolbarWithButton;

public class MyTokensPanel extends AbstractListPanel<TokenListItem, ActionModel>
{
  @SpringBean
  UiFacade facade;

  UserModel user;

  public MyTokensPanel(String id)
  {
    super(id, "tokenTable");
  }

  @Override
  protected void initColumns(TableBuilder<TokenListItem> builder)
  {
    builder.addColumn("action");
    builder.addColumn("token");
    builder.addColumn("url");
  }
  
  @Override
  protected void addToolbars(DataTable<TokenListItem> table,
      SortableServiceDataProvider<ActionModel, TokenListItem> provider)
  {
    AddToolbarWithButton invite = new AddToolbarWithButton("inviteUser", table, new TokenCreateUser());
    invite.setVisible(KonferenzSession.hasRight(Right.Actions.InviteUser));
    table.addTopToolbar(invite);
    super.addToolbars(table, provider);
  }

  protected void onSubmitForm()
  {
    setResponsePage(ConferencePage.class);
  }

  @Override
  protected TokenListItem transferDbToViewModel(ActionModel dbObject)
  {
    TokenListItem item = new TokenListItem();
    item.action = dbObject.getAction();
    item.token = dbObject.getToken();
    item.url = "/action/" + item.action + "/token/" + item.token;
    return item;
  }

  @Override
  protected Page<ActionModel> getItems(PageRequest request)
  {
    UserModel user = KonferenzSession.get().getCurrentUser();
    return facade.getUserActions(request, user);
  }

}
