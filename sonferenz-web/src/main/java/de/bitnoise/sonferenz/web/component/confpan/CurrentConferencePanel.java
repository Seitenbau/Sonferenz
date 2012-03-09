/*****************************************
Quelltexte zum Buch: Praxisbuch Wicket
(http://www.hanser.de/978-3-446-41909-4)

Autor: Michael Mosmann
(michael@mosmann.de)
 *****************************************/
package de.bitnoise.sonferenz.web.component.confpan;

import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import de.bitnoise.sonferenz.KonferenzSession;
import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.service.v2.exceptions.GeneralConferenceException;
import de.bitnoise.sonferenz.web.pages.statics.ConferencePage;
import de.bitnoise.sonferenz.web.utils.WicketTools;

public class CurrentConferencePanel extends Panel
{
  public CurrentConferencePanel(String id)
  {
    super(id);
    initPanel(); 
  }

  @SpringBean
  UiFacade facade;

  protected void initPanel()
  {
    try
    {
      Page<ConferenceModel> clist = facade.getAllConferences(new PageRequest(0, 5));
      createContent(clist.getContent());
    }
    catch (GeneralConferenceException e)
    {
      e.printStackTrace();
    }
  }

  private void createContent(final List<ConferenceModel> clist)
  {
    final ConferenceModel current = KonferenzSession.get().getCurrentConference();
    add(new ListView<ConferenceModel>("confList", clist) {
      @Override
      protected void populateItem(ListItem<ConferenceModel> item)
      {
        ConferenceModel iModel = item.getModelObject();
        BookmarkablePageLink<WebPage> link = new BookmarkablePageLink<WebPage>(
            "link", ConferencePage.class , ConferencePage.createParameters(iModel));
        item.add(link);
        Label label = new Label("name", Model.of(iModel.getShortName()));
        link.add(label);
        if ( iModel.getActive() ) {
          WicketTools.addCssClass( link , "active");
        }
        if(current!=null && current.getId().equals(iModel.getId())) {
          WicketTools.addCssClass( link , "current");
        }
      }
    });

  }

}
