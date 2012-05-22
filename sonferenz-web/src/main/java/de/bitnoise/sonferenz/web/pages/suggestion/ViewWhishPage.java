package de.bitnoise.sonferenz.web.pages.suggestion;

import org.apache.wicket.PageParameters;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.SuggestionModel;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;
import de.bitnoise.sonferenz.web.pages.suggestion.action.EditOrViewWhish;

@At(url = "/suggestion")
public class ViewWhishPage extends KonferenzPage
{
  public static final String PARAM_ID = "id";
  
  @SpringBean
  private UiFacade facade;

  public ViewWhishPage(PageParameters parameters)
  {
    super();
    int id = parameters.getInt(PARAM_ID);
    SuggestionModel whish = facade.getSuggestionById(id);
    Model model = new Model(whish);
    setResponsePage(new EditOrViewWhish().doAction(model));
  }
}
