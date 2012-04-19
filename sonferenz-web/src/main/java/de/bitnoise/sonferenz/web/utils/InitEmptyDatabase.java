package de.bitnoise.sonferenz.web.utils;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;

import org.apache.wicket.markup.html.basic.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.bitnoise.sonferenz.KonferenzDefines;
import de.bitnoise.sonferenz.model.UserRole;
import de.bitnoise.sonferenz.model.UserRoles;
import de.bitnoise.sonferenz.repo.RoleRepository;
import de.bitnoise.sonferenz.service.v2.events.ConfigReload;
import de.bitnoise.sonferenz.service.v2.services.ConfigurationService;
import de.bitnoise.sonferenz.service.v2.services.StaticContentService;
import de.bitnoise.sonferenz.service.v2.services.UserService;
import de.bitnoise.sonferenz.service.v2.services.idp.provider.local.LocalIdp;
import de.bitnoise.sonferenz.service.v2.services.impl.EventingImpl;

@Service
public class InitEmptyDatabase
{
	private static final String INIT_MARKER_KEY = "intern.database-is-initialized";

	private static final Logger logger = LoggerFactory
	        .getLogger(InitEmptyDatabase.class);

	@Autowired
	ConfigurationService config;

	@Autowired
	StaticContentService texte;

	@Autowired
	UserService user;

	@Autowired
	EventingImpl eventing;

	@Autowired
	RoleRepository roles;

	@PostConstruct
	@Transactional(noRollbackFor = Exception.class)
	public void initAemptyDatabase()
	{
		try
		{
			eventing.disableEventing();
			if (isDatabaseOlderThanVersion(1))
			{
				initEmptyDatabase();
			}
			if (isDatabaseOlderThanVersion(2))
			{
				updateTo_v0_2_1();
			}
		} finally
		{
			eventing.activateEventing();
			eventing.post(new ConfigReload());
		}
	}

	void updateTo_v0_2_1() {
		logger .warn("# DATABASE update to version 2 #");
		config.saveIntegerValue(INIT_MARKER_KEY, 2);
		// since 0.2.1
		texte.storeText("display.MyProfilePanel$UserUnique", "Benutzer Name schon vergeben");
		texte.storeText("table.tokenTable.column.state", "Zustand");
		texte.storeText("table.tokenTable.column.title", "Beschreibung");
		texte.storeText("table.tokenTable.column.url", "Link");
		texte.storeText("table.tokenTable.column.expires", "Gueltigkeit");
		texte.storeText("profile.tab.overview", "Profil");
		texte.storeText("table.myTalks.column.Titel", "Meine Vorträge");
		texte.storeText("table.myWhishes.column.Titel", "Meine Wünsche");
		texte.storeText("menu.1lvl.info", "Konferenz");
		texte.storeText("username.StringValidator.maximum", "Benutzername zu lang");
		texte.storeText("table.users.column.Login", "Login");
		texte.storeText("email.StringValidator.maximum", "eMail zu lang");
		texte.storeText("display.SubscribeActionPanel$UserUnique", "Benutzername existiert schon");
		texte.storeText("password2.EqualPasswordInputValidator", "Passwörter nicht identisch");
    texte.storeText("display.PatternValidator", "Display Name entspricht nicht Muster : " + KonferenzDefines.REGEX_USER_DISPLAY);
    texte.storeText("login.PatternValidator", "Login Name entspricht nicht Muster : " + KonferenzDefines.REGEX_USERNAME);
    
    texte.storeText("action.subscribe.confirm.mail.body", "You've subscribed as $${username} $${email} $${url}");
    texte.storeText("mailbody.InviteUserPanel$ContainsToken", "Missing $${link} in body");
    texte.storeText("profile.user.invites", "Einladung erstellen");
    
    texte.storeText("page.profile.invite.header", "<p/>");
    texte.storeText("page.profile.talk.header", "");
    texte.storeText("page.profile.whish.header", "");

		createRole(4, "INVITE");
	}

	void initEmptyDatabase() {
		logger
		        .warn("#################################################################");
		logger
		        .warn("# DATABASE SEEMS TO BE EMPTY ... initializing with default Data #");
		logger.warn("# ");
		config.initValue(INIT_MARKER_KEY, 1);
		config.initValue("smtp.host", "localhost");
		config.initValue("mail.create.from", "sonferenz");
		config.initValue("mail.create.replyTo", "sonferenz@localhost");
		config.initValue("baseUrl", "http://localhost:8080/sonferenz-web");

		logger.warn("config created");

		texte.storeText("menu.MyProfile", "Profile");
		texte.storeText("menu.Talks", "Vorträge");
		texte.storeText("menu.Whishes", "Wünsche");
		texte.storeText("menu.Profile", "Profil");
		texte.storeText("menu.Administration", "Administration");
		texte.storeText("menu.Logout", "Logout");
		texte.storeText("menu.Timetable", "Zeitplan");
		texte.storeText("menu.Home", "Home");
		texte.storeText("menu.null", "");
		texte.storeText("menu.Add", "Add");

		texte.storeText("menu.1lvl.conference", "Konferenz");
		texte.storeText("menu.1lvl.whishes", "Wünsche");
		texte.storeText("menu.1lvl.talks", "Vorträge");
		texte.storeText("menu.1lvl.contact", "Kontakt");
		texte.storeText("menu.1lvl.register", "Anmeldung");

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

		texte.storeText("table.whish.column.Like", "Like");
		texte.storeText("table.whish.column.Titel", "Titel");
		texte.storeText("table.whish.column.Description", "Beschreibung");
		texte.storeText("table.whish.column.Verantwortlicher", "Author");
		texte.storeText("table.whish.create", "Neuen Wunsch anlegen");

		texte.storeText("table.talks.column.Titel", "Titel");
		texte.storeText("table.talks.column.Description", "Beschreibung");
		texte.storeText("table.talks.column.Author", "Author");
		texte.storeText("table.talks.create", "Neuen Vortrag einreichen");

		texte.storeText("profile.tab.profile", "Einstellungen");
		texte.storeText("profile.tab.invites", "Einladen");
		texte.storeText("profile.user.invites", "Neuen Benutzer einladen");

		texte.storeText("table.tokenTable.column.action", "Aktion");
		texte.storeText("table.tokenTable.column.token", "Token");
		texte.storeText("table.tokenTable.column.url", "Url");

		texte.storeText("admin.tab.users", "Benutzer");
		texte.storeText("admin.tab.roles", "Rollen");
		texte.storeText("admin.tab.conference", "Konferenzen");
		texte.storeText("admin.tab.config", "Konfiguration");
		texte.storeText("admin.tab.i18n", "Texte");
		texte.storeText("table.user.createLink", "Neuen Benutzer anlegen");

		texte.storeText("table.TexteTable.column.name", "text id");
		texte.storeText("table.TexteTable.column.value", "text");

		texte.storeText("org.apache.wicket.extensions.wizard.previous", "Zurück");
		texte.storeText("org.apache.wicket.extensions.wizard.next", "Weiter");
		texte
		        .storeText("org.apache.wicket.extensions.wizard.cancel", "Abbrechen");
		texte.storeText("org.apache.wicket.extensions.wizard.finish",
		        "Fertigstellen");
		texte.storeText("table.whish.hint.Like1", "");

		texte.storeText("username.InviteUserPanel$UserUnique",
		        "Benutzername muss eindeutig sein");
		texte.storeText("username.Required",
		        "Ein Benutzername muss angegeben werden");
		texte.storeText("email.Required", "Eine email Addresse wird benötigt");
		texte.storeText("email.InviteUserPanel$EMailNotUsed",
		        "email Addresse muss eindeutig sein");
		texte.storeText("email.EmailAddressValidator",
		        "email Addresse ist ungültig");
		texte.storeText("password1.Required", "Passwort benötigt");
		texte.storeText("password2.Required", "Passwort Bestätigung benötigt");
		texte.storeText("password1.PatternValidator", "Passwort entspricht nicht Muster : " + KonferenzDefines.PASSWORD_REGEX);
		texte.storeText("display.Required", "Displayname wird benötigt");
		texte.storeText("display.PatternValidator", "Display Name entspricht nicht Muster : " + KonferenzDefines.REGEX_USER_DISPLAY);
		texte.storeText("login.PatternValidator", "Login Name entspricht nicht Muster : " + KonferenzDefines.REGEX_USERNAME);
		texte.storeText("title.Required", "Bitte Titel eingeben");
		texte.storeText("title.StringValidator.maximum", "Titel zu lang");
		texte.storeText("author.StringValidator.maximum", "Author zu lang");
		texte.storeText("theOwner.Required",
		        "Bearbeitungsrechte müssen vergebene werden");
		texte.storeText("description.StringValidator.maximum",
		        "Beschreibung zu lang");
		texte.storeText("action.success", "Your account has been created.");
		texte.storeText("title.Required", "Titel muss angegeben werden");

		texte.storeText("conferenceState.null", "- none -");
		texte.storeText("CREATED", "Created");
		texte.storeText("PLANNING", "Planning");
		texte.storeText("VOTING", "Voting");
		texte.storeText("PLANNED", "Planned");

		texte.storeText("page.home",
		        "page.home: as admin you can change this text.");
		texte.storeText("page.agenda",
		        "page.agenda: as admin you can change this text.");
		texte.storeText("page.review",
		        "page.review: as admin you can change this text.");
		texte.storeText("page.register",
		        "page.register: as admin you can change this text.");
		texte.storeText("page.contact",
		        "page.contact: as admin you can change this text.");
		texte.storeText("page.info",
		        "page.info: as admin you can change this text.");

		texte.storeText("action.subscribe.mail.subject", "You have been invited to SDC!");
		texte.storeText("action.subscribe.mail.body", "Follow this link to complete: ${link}");
		texte.storeText("action.verify.mail.subject", "Details for your new user account");
		texte.storeText("action.verify.mail.body", "Please confirm your email by open the folling link in your browser: {link}");

		logger.warn("texts created");

		createRole(1, "ADMIN");
		createRole(2, "USER");
		createRole(3, "MANAGER");

		logger.warn("roles created");

		Collection<UserRoles> newRoles = new ArrayList<UserRoles>();
		newRoles.add(UserRoles.USER);
		newRoles.add(UserRoles.MANAGER);
		newRoles.add(UserRoles.ADMIN);
		user.createIdentity(LocalIdp.IDP_NAME, "admin", "admin", "admin",
		        "admin@localhost", newRoles);
		logger.warn("users created");
		logger
		        .warn("#################################################################");
	}

	public void createRole(int id, String name)
	{
		UserRole role = new UserRole();
		role.setId(id);
		role.setName(name);
		roles.save(role);
	}

	private boolean isDatabaseOlderThanVersion(int minVersion) {
		Integer val = config.getIntegerValue(-1, INIT_MARKER_KEY);
		return val < minVersion;
	}

}
