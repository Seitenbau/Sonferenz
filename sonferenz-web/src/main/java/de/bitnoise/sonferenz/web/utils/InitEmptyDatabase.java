package de.bitnoise.sonferenz.web.utils;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.bitnoise.sonferenz.model.UserRole;
import de.bitnoise.sonferenz.model.UserRoles;
import de.bitnoise.sonferenz.repo.RoleRepository;
import de.bitnoise.sonferenz.service.v2.services.ConfigurationService;
import de.bitnoise.sonferenz.service.v2.services.StaticContentService;
import de.bitnoise.sonferenz.service.v2.services.UserService;
import de.bitnoise.sonferenz.service.v2.services.idp.provider.local.LocalIdp;

@Service
public class InitEmptyDatabase
{
  private static final String INIT_MARKER_KEY = "intern.database-is-initialized";

  @Autowired
  ConfigurationService config;

  @Autowired
  StaticContentService texte;
  
  @Autowired
  UserService user;
  
  @Autowired
  RoleRepository roles;

  @PostConstruct
  public void initAemptyDatabase()
  {
    if (isDatabaseAllreadyInitialized())
    {
      return;
    }

    config.initValue(INIT_MARKER_KEY, 1);
    config.initValue("smtp.host", "localhost");
    config.initValue("mail.create.from", "soferenz@localhost");
    config.initValue("baseUrl", "http://localhost:8080/sonferenz-web");

    texte.storeText("menu.MyProfile", "Profile");
    texte.storeText("menu.Talks", "Talks");
    texte.storeText("menu.Whishes", "Whishes");
    texte.storeText("menu.Conference", "Conference");
    texte.storeText("menu.Config", "Config");
    texte.storeText("menu.Roles", "Roles");
    texte.storeText("menu.Users", "Users");
    texte.storeText("menu.Texte", "Texte");
    texte.storeText("menu.Logout", "Logout");
    texte.storeText("menu.Timetable", "Timetable");
    texte.storeText("menu.Home", "Home");
    texte.storeText("menu.null", "");
    texte.storeText("menu.Add", "Add");
    texte.storeText("datatable.no-records-found", "No records");
    texte.storeText("toolbar.user.LogoutPage.label", "Logout");
    texte.storeText("toolbar.user.LogoutPage.prefix", "");
    texte.storeText("toolbar.user.LoginPage.label", "Login");
    texte.storeText("toolbar.user.LoginPage.prefix", "");
    texte.storeText("table.GlobalConfig.column.name", "");
    texte.storeText("table.GlobalConfig.column.value", "Value");
    texte.storeText("table.users.column.Name", "Name");
    texte.storeText("table.users.column.Provider", "Provider");
    texte.storeText("table.users.column.Rollen", "rollen");
    texte.storeText("table.ListRoles.column.name", "Name");
    texte.storeText("table.conference.column.Aktiv", "Aktiv");
    texte.storeText("table.conference.column.Status", "Status");
    texte.storeText("table.conference.column.Name", "Name");
    
    texte.storeText("conferenceState.null", "- none -");
    texte.storeText("CREATED", "Created");
    texte.storeText("PLANNING", "Planning");
    texte.storeText("VOTING", "Voting");
    texte.storeText("PLANNED", "Planned");

    createRole(1,"ADMIN");
    createRole(2,"USER");
    createRole(3,"MANAGER");
    
    Collection<UserRoles> newRoles = new ArrayList<UserRoles>();
    newRoles.add(UserRoles.USER);
    newRoles.add(UserRoles.MANAGER);
    newRoles.add(UserRoles.ADMIN);
    user.createIdentity(LocalIdp.IDP_NAME,"admin", "admin", "admin@localhost", newRoles);
  }

  public void createRole(int id, String name)
  {
    UserRole role=new UserRole();
    role.setId(id);
    role.setName(name);
    roles.save(role );
  }

  boolean isDatabaseAllreadyInitialized()
  {
    Integer val = config.getIntegerValue(-1, INIT_MARKER_KEY);
    return val != -1;
  }
}
