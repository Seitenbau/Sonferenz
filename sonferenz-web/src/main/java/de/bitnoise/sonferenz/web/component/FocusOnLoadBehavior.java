package de.bitnoise.sonferenz.web.component;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;

/**
 * source :
 * https://cwiki.apache.org/WICKET/request-focus-on-a-specific-form-component.html
 * 
 * site : https://cwiki.apache.org/WICKET url :
 * /request-focus-on-a-specific-form-component.html
 */
public class FocusOnLoadBehavior extends Behavior
{
  private Component component;

  public void bind(Component component)
  {
    this.component = component;
    component.setOutputMarkupId(true);
  }
  
  @Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
	}
// ##RW## wicket 6
//  public void renderHead(IHeaderResponse iHeaderResponse)
//  {
//    super.renderHead(iHeaderResponse);
//    iHeaderResponse.renderOnLoadJavascript("document.getElementById('"
//        + component.getMarkupId() + "').focus()");
//  }

  public boolean isTemporary()
  {
    return true;
  }
}