package de.bitnoise.sonferenz.web.component.table;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

import de.bitnoise.sonferenz.web.pages.voting.UserVoteItem;

public abstract class VoteColumn extends AbstractColumn<UserVoteItem,SortParam<String>>
{
  
  public VoteColumn(IModel<String> displayModel)
  {
    super(displayModel);
  }

  public void populateItem(Item<ICellPopulator<UserVoteItem>> cellItem,
      String componentId, IModel<UserVoteItem> rowModel)
  {
    cellItem.add(new VoteColumnPanel(componentId, rowModel)
    {

      @Override
      protected void onUnVote(AjaxRequestTarget target, IModel<UserVoteItem> row)
      {
        unVote(target, row);
      }

      @Override
      protected void onVote(AjaxRequestTarget target, IModel<UserVoteItem> row)
      {
        vote(target, row);
      }

    });
  }

  protected abstract void vote(AjaxRequestTarget target,
      IModel<UserVoteItem> row);

  protected abstract void unVote(AjaxRequestTarget target,
      IModel<UserVoteItem> row);

}
