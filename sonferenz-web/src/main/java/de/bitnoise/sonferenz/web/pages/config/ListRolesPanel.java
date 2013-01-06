package de.bitnoise.sonferenz.web.pages.config;

import java.io.Serializable;

import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.UserRole;
import de.bitnoise.sonferenz.web.component.TableBuilder;
import de.bitnoise.sonferenz.web.pages.base.AbstractListPanel;

class ViewModel implements Serializable
{
  String name;
}

public class ListRolesPanel extends AbstractListPanel<ViewModel, UserRole>
{
  @SpringBean
  UiFacade facade;

  public ListRolesPanel(String id)
  {
    super(id, "ListRoles");
  }

  @Override
  protected void initColumns(TableBuilder<ViewModel> builder)
  {
    builder.addColumn("name");
  }

  @Override
  protected ViewModel transferDbToViewModel(UserRole dbObject)
  {
    ViewModel view = new ViewModel();
    view.name = dbObject.getName();
    return view;
  }

  @Override
  protected Page<UserRole> getItems(PageRequest request)
  {
    return facade.getAllRoles(request);
  }

}
