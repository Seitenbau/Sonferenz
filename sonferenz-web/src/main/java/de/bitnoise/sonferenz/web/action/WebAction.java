package de.bitnoise.sonferenz.web.action;

import org.apache.wicket.model.IModel;

public abstract class WebAction<T extends IModel<?>>  implements IWebAction<T>
{
  public boolean isVisible()
  {
    return true;
  }
}
