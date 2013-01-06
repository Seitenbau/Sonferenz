package de.bitnoise.sonferenz.web.component.table;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.rating.RatingPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;


public abstract class RatingBoxColumn<T> extends AbstractColumn<T,SortParam<String>>  
   
{
  private int _nrOfStars;

  public RatingBoxColumn(IModel<String> displayModel, int nrOfStarts)
  {
    super(displayModel);
    _nrOfStars = nrOfStarts;
  }

  public void populateItem(Item<ICellPopulator<T>> cellItem,
      String componentId, final IModel<T> rowModel)
  {
    final RatingModel r = newRatingModel(rowModel);
    cellItem.add(new RatingPanel(componentId, Model.of(r.getRating()),
        _nrOfStars, Model.of(r.getNrOfVotes()), r.hasVoted()) {
      @Override
      protected boolean onIsStarActive(int star)
      {
        return star < r.getCurrentVote();
      }

      @Override
      protected void onRated(int rating, AjaxRequestTarget target)
      {
        r.onRated(rowModel, rating, target);
      }
    });
  }

  protected abstract RatingModel<T> newRatingModel(IModel<T> rowModel);

  public abstract static class RatingModel<T> implements Serializable
  {
    public abstract void onRated(IModel<T> rowModel, int rating,
        AjaxRequestTarget target);

    public abstract int getCurrentVote();

    public abstract Integer getRating();

    public abstract boolean hasVoted();

    public abstract Integer getNrOfVotes();
  }
 
}