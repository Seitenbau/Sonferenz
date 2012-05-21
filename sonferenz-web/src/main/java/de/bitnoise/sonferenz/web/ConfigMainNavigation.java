/*****************************************
 * Quelltexte zum Buch: Praxisbuch Wicket
 * (http://www.hanser.de/978-3-446-41909-4)
 * 
 * Autor: Michael Mosmann (michael@mosmann.de)
 *****************************************/
package de.bitnoise.sonferenz.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Page;

import de.bitnoise.sonferenz.web.app.Right;
import de.bitnoise.sonferenz.web.component.navigation.AbstractNavCallback;
import de.bitnoise.sonferenz.web.component.navigation.NavCallbackInterface;
import de.bitnoise.sonferenz.web.component.navigation.PageNavCallback;
import de.bitnoise.sonferenz.web.component.state.AllwaysVisible;
import de.bitnoise.sonferenz.web.component.state.IsActiveConference;
import de.bitnoise.sonferenz.web.component.state.IsLoggedIn;
import de.bitnoise.sonferenz.web.component.state.OnStateVoting;
import de.bitnoise.sonferenz.web.component.state.VisibleOnRights;
import de.bitnoise.sonferenz.web.pages.admin.AdminPage;
import de.bitnoise.sonferenz.web.pages.auth.LogoutPage;
import de.bitnoise.sonferenz.web.pages.calculate.CalculateOverviewPage;
import de.bitnoise.sonferenz.web.pages.conference.ConferenceOverviewPage;
import de.bitnoise.sonferenz.web.pages.config.EditConfigurationPage;
import de.bitnoise.sonferenz.web.pages.config.EditTextePage;
import de.bitnoise.sonferenz.web.pages.config.EditUserRolesPage;
import de.bitnoise.sonferenz.web.pages.profile.MyProfilePage;
import de.bitnoise.sonferenz.web.pages.proposal.TalksOverviewPage;
import de.bitnoise.sonferenz.web.pages.schedule.SchedulePage;
import de.bitnoise.sonferenz.web.pages.settings.SettingsPage;
import de.bitnoise.sonferenz.web.pages.statics.ConferencePage;
import de.bitnoise.sonferenz.web.pages.suggestion.WhishOverviewPage;
import de.bitnoise.sonferenz.web.pages.users.UserOverviewPage;
import de.bitnoise.sonferenz.web.pages.voting.VotingOverviewPage;

public class ConfigMainNavigation
{
  public static List<NavCallbackInterface> getPages()
  {
    List<NavCallbackInterface> ret = new ArrayList<NavCallbackInterface>();
//    ret.add(new PageNavCallback(ConferencePage.class, "Home", new AllwaysVisible()));

    // users
    /*
    ret.add(new PageNavCallback(WhishOverviewPage.class, "Whishes",
        new VisibleOnRights(Right.Whish.List)
        ));
    ret.add(new PageNavCallback(TalksOverviewPage.class, "Talks",
        new VisibleOnRights(Right.Talk.List),
        new IsActiveConference()
        ));
        */
//    ret.add(new PageNavCallback(VotingOverviewPage.class, "Voting",
//        new VisibleOnRights(Right.Vote.canVote),
//        new IsActiveConference(),
//        new OnStateVoting()
//        ));
//    ret.add(new PageNavCallback(CalculateOverviewPage.class, "Calculate",
//        new VisibleOnRights(Right.Admin.ViewCalculation),
//        new IsActiveConference(),
//        new OnStateVoting()
//        ));
    ret.add(new PageNavCallback(MyProfilePage.class, "Profile",
        new IsLoggedIn()
        ));
    
    // admin
    ret.add(new PageNavCallback(AdminPage.class, "Administration",
    		new VisibleOnRights(Right.Admin.Configure)));
    
    ret.add(new PageNavCallback(LogoutPage.class, "Logout",
            new IsLoggedIn()
            ));
    return ret;
  }

  public static NavCallbackInterface getMainNaviagtion()
  {
    return new AbstractNavCallback(null)
    {
      public List<NavCallbackInterface> getChilds(Page page)
      {
        return getPages();
      }

      public void onClick(Page page)
      {
      }
    };
  };
}
