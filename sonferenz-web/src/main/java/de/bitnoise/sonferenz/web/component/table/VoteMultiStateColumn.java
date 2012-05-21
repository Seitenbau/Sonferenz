package de.bitnoise.sonferenz.web.component.table;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import com.visural.wicket.behavior.beautytips.BeautyTipBehavior;
import com.visural.wicket.behavior.beautytips.TipPosition;

import de.bitnoise.sonferenz.web.app.WicketApplication;

public abstract class VoteMultiStateColumn<T extends IMutiState> extends
    AbstractColumn<T>
{
  public VoteMultiStateColumn(IModel<String> displayModel)
  {
    super(displayModel);
  }

  public void populateItem(Item<ICellPopulator<T>> cellItem,
      String componentId, IModel<T> rowModel)
  {
    cellItem.add(new MultiStatePanel(componentId, rowModel));
  }

  @Override
  public Component getHeader(String componentId)
  {
    Component hdr = super.getHeader(componentId);
    StringBuilder sb = new StringBuilder();
    sb.append(new ResourceModel("hint.state.top").getObject());
    sb.append("<ul style='list-style-type:none;margin-left: 0; padding-left: 0.2em;'>");
    addState(sb, img2, "hint.state.2");
    addState(sb, img1, "hint.state.1");
    addState(sb, img0, "hint.state.0");
    sb.append("</ul>");
    String _hint = sb.toString();
    if (_hint != null)
    {
      hdr.add(new BeautyTipBehavior(_hint)
          .setPositionPreference(TipPosition.bottom));
    }
    return hdr;
  }

  private void addState(StringBuilder sb, ResourceReference img, String txtId)
  {
    sb.append("<li><img src=\"resources/");
    sb.append(img.getScope().getCanonicalName());
    sb.append("/");
    sb.append(img.getName());
    sb.append("\"/>&nbsp;");
    sb.append(new ResourceModel(txtId).getObject());
    sb.append("</li>");
  }

  protected abstract <T extends IMutiState> boolean onChange(
      AjaxRequestTarget target, IModel<T> model, int next);

  final ResourceReference img0 = new ResourceReference(WicketApplication.class,"images/state-0.gif");
  final ResourceReference img1 = new ResourceReference(WicketApplication.class,"images/state-1.gif");
  final ResourceReference img2 = new ResourceReference(WicketApplication.class,"images/state-2.gif");

  public class MultiStatePanel<T extends IMutiState> extends Panel
  {
    public MultiStatePanel(String id, final IModel<T> model)
    {
      super(id, model);

      ResourceReference ref = getRef(model);

      final Image img = new Image("image", ref);
      img.setOutputMarkupId(true);

      AjaxFallbackLink<String> link = new AjaxFallbackLink<String>("link",
          Model.of("TheLink")) {
        @Override
        public void onClick(AjaxRequestTarget target)
        {
          int old = model.getObject().getMutiStateValue();
          int next = ++old % 3;

          if (onChange(target, model, next))
          {
            model.getObject().setMutiStateValue(next);
            ResourceReference ref = getRef(model);
            img.setImageResourceReference(ref);
            target.addComponent(img);
          }
          else if (next == 2)
          {
            next = 0;
            model.getObject().setMutiStateValue(next);
            ResourceReference ref = getRef(model);
            img.setImageResourceReference(ref);
            target.addComponent(img);
            onChange(target, model, next);
          }
        }

      };

      add(link);
      link.add(img);
    }

    protected ResourceReference getRef(IModel<T> model)
    {
      switch (model.getObject().getMutiStateValue())
      {
      case 0:
        return img0;
      case 1:
        return img1;
      case 2:
        return img2;
      default:
        return img1;
      }
    }
  }
}
