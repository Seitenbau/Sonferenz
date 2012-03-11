package de.bitnoise.sonferenz.web.component.table;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.bitnoise.sonferenz.service.v2.services.StaticContentService;

public abstract class ListPanel<T> extends Panel
{
  private static final long serialVersionUID = 1L;

  protected int _maxRowsPerPage;

  protected FeedbackPanel feedback;

  public ListPanel(String id)
  {
    super(id);
    InjectorHolder.getInjector().inject(this);
    _maxRowsPerPage = 100;
  }

  @Override
  protected void onInitialize()
  {
    super.onInitialize();
    SortableDataProvider<T> provider = createDataProviderProvider();
    List<IColumn<T>> columns = createColumns();
    DefaultDataTable<T> table = new DefaultDataTable<T>(
        "contentTable", columns, provider, getMaxRowPerPage());

    add(createHeaderText("headerText"));
    add(table);
    feedback = new FeedbackPanel("feedback");
    add(feedback);
  }

  @SpringBean
  StaticContentService content2;

  protected Component createHeaderText(String id)
  {
    String html = content2.text("header." + getHeaderKey());
    if (html == null)
    {
      html = content2.text("header.default");
      if (html == null)
      {
        html = "";
      }
    }
    return new Label(id, html).setEscapeModelStrings(false);
  }

  protected String getHeaderKey()
  {
    return this.getClass().getSimpleName();
  }

  protected int getMaxRowPerPage()
  {
    return _maxRowsPerPage;
  }

  protected abstract List<IColumn<T>> createColumns();

  protected abstract SortableDataProvider<T> createDataProviderProvider();
}
