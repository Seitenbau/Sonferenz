package de.bitnoise.sonferenz.web.pages.voting;

import java.util.ArrayList;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import wicketdnd.DragSource;
import wicketdnd.DropTarget;
import wicketdnd.Location;
import wicketdnd.Operation;
import wicketdnd.Reject;
import wicketdnd.Transfer;
import wicketdnd.theme.WindowsTheme;

public class ListVotesPanel2 extends Panel {


	public ListVotesPanel2(String id) {
		super(id);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(CSSPackageResource.getHeaderContribution(new WindowsTheme()));
		final WebMarkupContainer   list =new WebMarkupContainer("voting");
		final VoteList foos = new VoteList();
		foos .add("Rainer 1");
		foos .add("Rainer 3");
		foos .add("Rainer 2");
		ListView<String> items = new ListView<String>("items", foos) {
			@Override
			protected ListItem<String> newItem(int index) {
				ListItem<String> item = super.newItem(index);
				item.setOutputMarkupId(true);
				return item;
			}

			@Override
			protected void populateItem(ListItem<String> item) {
				item.add(new Label("name", Model.of(item.getModel().getObject())));
			}
		};
		
		list.add(items);
		
		list.add(new DragSource(Operation.MOVE)
		{

			@Override
			public void onAfterDrop(AjaxRequestTarget target, Transfer transfer)
			{
				if (transfer.getOperation() == Operation.MOVE)
				{
					foos.remove(transfer.getData());

					target.addComponent(list);
				}
			}
		}.drag("div.item").initiate("span.initiate"));
		
		DropTarget dropTarget = new DropTarget(Operation.MOVE)
		{
			@Override
			public void onDrop(AjaxRequestTarget target, Transfer transfer, Location location) throws Reject {
				if (location.getComponent() == list)
				{
					foos.add(operate(transfer));
				}
				else
				{
					String foo = location.getModelObject();
					switch (location.getAnchor())
					{
						case TOP :
						case LEFT :
							foos.addBefore(operate(transfer), foo);
							break;
						case BOTTOM :
						case RIGHT :
							foos.addAfter(operate(transfer), foo);
							break;
						default :
							transfer.reject();
					}

					target.addComponent(list);
				}
			}
		}.dropTopAndBottom("div.item");
		
		list.add(dropTarget);
		add(list);
		AjaxFallbackLink<String> save = new AjaxFallbackLink<String>("save",Model.of("Save changes")){
			@Override
            public void onClick(AjaxRequestTarget target) {
				onSave(target);
            }};
		add(save);
	}

	protected void onSave(AjaxRequestTarget target) {
	    
    }	

	class VoteList extends ArrayList<String> {
		public void addBefore(String drag, String drop)
		{
//			drag.remove();
			add(indexOf(drop), drag);
		}
		
		public void addAfter(String drag, String drop)
		{
//			drag.remove();
			add(indexOf(drop) + 1, drag);
		}
	}
	
	protected String operate(Transfer transfer)
	{
		String foo = transfer.getData();
		return foo;
//		switch (transfer.getOperation())
//		{
//			case MOVE :
//			case COPY :
//				return foo.copy();
//			case LINK :
//				return foo.link();
//			default :
//				throw new IllegalArgumentException();
//		}
	}
}
