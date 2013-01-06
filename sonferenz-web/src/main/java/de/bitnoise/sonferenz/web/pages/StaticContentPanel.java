package de.bitnoise.sonferenz.web.pages;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.bitnoise.sonferenz.service.v2.services.StaticContentService;
import de.bitnoise.sonferenz.web.app.KonferenzSession;
import de.bitnoise.sonferenz.web.app.Right;

public class StaticContentPanel extends Panel
{

  private String _key;

  public StaticContentPanel(String panelId, String textKey)
  {
    super(panelId);
    _key = textKey;
    InjectorHolder.getInjector().inject(this);
  }

  @SpringBean
  StaticContentService content2;

  @Override
  protected void onInitialize()
  {
    super.onInitialize();
    String html = content2.text(_key);
    if (html == null)
    {
      html = "<!-- Empty Page " + _key + " -->";
    }
    add(new Label("text", html).setEscapeModelStrings(false));
    PageParameters parameters = new PageParameters();
    parameters.add("id", _key);
    BookmarkablePageLink<String> editLink = new BookmarkablePageLink<String>(
        "editLink", StaticContentEditPage.class, parameters);
    boolean vis = KonferenzSession.hasRight(Right.Actions.EditStaticPage);
    editLink.setVisible(vis);
    add(editLink);
  }
}
