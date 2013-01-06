package de.bitnoise.sonferenz.web.pages.suggestion;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

import de.bitnoise.sonferenz.web.app.WicketApplication;

public class LikePanel extends Panel {

	private Model<Boolean> _model;
	private ResourceReference _imodel;
    private Model<Integer> _globalCount;

	public LikePanel(String id, boolean starOn,Integer globalCount) {
		super(id);
		_model = Model.of(starOn);
		_globalCount = Model.of(globalCount);
	}

	public LikePanel(String id, Model<Boolean> _model2) {
		super(id);
		_model = _model2;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		setOutputMarkupId(true);
		final ResourceReference refOn = new PackageResourceReference(
		    WicketApplication.class,
				"images/sun_on.gif");
		final ResourceReference refOff = new PackageResourceReference(
		    WicketApplication.class,
				"images/sun_off.gif");
		ResourceReference ref = refOn;
		if (_model.getObject().booleanValue()) {
			ref = refOn;
		} else {
			ref = refOff;
		}
		final Image img = new Image("image", ref);
		img.setOutputMarkupId(true);
		final Label count=new Label("count",_globalCount);
		count.setOutputMarkupId(true);
		AjaxFallbackLink<String> link = new AjaxFallbackLink<String>("link") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				boolean old = _model.getObject().booleanValue();
				Boolean next = new Boolean(!old);
				_model.setObject(!old);
				onChange(target,_model);
				if (_model.getObject()) {
					img.setImageResourceReference(refOn);
				} else {
					img.setImageResourceReference(refOff);
				}
				target.add(img);
				target.add(count);
			}
		};
		
        add(count);
		add(link);
		link.add(img);
	}

	protected void onChange(AjaxRequestTarget target, Model<Boolean> newValue) {
		
	}

}
