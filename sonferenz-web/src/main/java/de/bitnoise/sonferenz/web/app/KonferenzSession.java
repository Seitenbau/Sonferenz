package de.bitnoise.sonferenz.web.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.wicket.Session;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.CommunicationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.model.UserRole;
import de.bitnoise.sonferenz.model.UserRoles;
import de.bitnoise.sonferenz.service.v2.model.AppContext;
import de.bitnoise.sonferenz.service.v2.services.AuthenticationService;

public class KonferenzSession extends WebSession
{
  private static List<String> userRoles = new ArrayList<String>();

  private static List<String> managerRoles = new ArrayList<String>();
  
  private static List<String> inviteRoles = new ArrayList<String>();
  
  private static List<String> adminRoles = new ArrayList<String>();

  private static ConferenceModel _conference;
  
  private static KonferenzSession _TestMockSession;

  @SpringBean
  AuthenticationService authService;

  @SpringBean
  UiFacade facade;

  @SpringBean(name = "springAuthManager")
  AuthenticationManager authenticationManager;

  private ConferenceModel _currentConference;

  public KonferenzSession(Request request)
  {
    super(request);
    InjectorHolder.getInjector().inject(this);
    managerRoles.add(Right.Actions.InviteUser);
    managerRoles.add(Right.Actions.ManageInviteUser);
    managerRoles.add(Right.Actions.SpeakerEditAny);
    managerRoles.add(Right.Actions.EditStaticPage);
    inviteRoles.add(Right.Actions.InviteUser);
    
    adminRoles.add(Right.User.List);
    adminRoles.add(Right.User.Create);
    adminRoles.add(Right.User.Edit);
    adminRoles.add(Right.Conference.List);
    adminRoles.add(Right.Conference.Create);
    adminRoles.add(Right.Conference.Edit);
    adminRoles.add(Right.Admin.ViewCalculation);
    adminRoles.add(Right.Admin.StartCalculation);
    adminRoles.add(Right.Admin.Configure);
    adminRoles.add(Right.Actions.SpeakerEditAny);
    adminRoles.add(Right.Actions.EditStaticPage);

    userRoles.add(Right.Talk.List);
    userRoles.add(Right.Talk.Create);
    userRoles.add(Right.Talk.Edit);
    userRoles.add(Right.Whish.List);
    userRoles.add(Right.Whish.Create);
    userRoles.add(Right.Whish.Edit);
    userRoles.add(Right.Vote.canVote);
  }

  public static KonferenzSession get()
  {
    if (_TestMockSession != null)
    {
      return _TestMockSession;
    }
    return (KonferenzSession) Session.get();
  }

  public UserModel getCurrentUser()
  {
    return authService.getCurrentUser();
  }

  public String authenticate(String username, String password)
  {
    Logger logger = LoggerFactory.getLogger(KonferenzSession.class);
    try
    {
      logger.info("Start login for " + username);
      Authentication request = new UsernamePasswordAuthenticationToken(
          username, password);
      logger.info("authenticate() for " + username);
      Authentication result = authenticationManager.authenticate(request);
      logger.info("setContext() for " + username);
      SecurityContextHolder.getContext().setAuthentication(result);
      Thread t = Thread.currentThread();
      SecurityContext c = SecurityContextHolder.getContext();
      Authentication a = c.getAuthentication();
      logger.info("Login done for " + username);
      return null;
    }
    catch (BadCredentialsException e)
    {
      logger.debug("BadCredentialsException error.", e);
      return "Bad credentials";
    }
    catch (AuthenticationException e)
    {
      logger.warn("Error at login", e);
      if (e.getCause() instanceof CommunicationException)
      {
        return "Error while communication with backend.";
      }
      return "Error at login";
    }
    catch (Throwable t) 
    {
      logger.error("Error at login", t);
      return "Error at login!";
    }
  }

  public static boolean hasRight(String... requestedRight)
  {
    if (requestedRight == null)
    {
      return false;
    }
    UserModel currentUser = KonferenzSession.get().getCurrentUser();
    if (currentUser == null)
    {
      return false;
    }

    for (String right : requestedRight)
    {
      if (has(currentUser, right))
      {
        return true;
      }
    }
    return false;
  }

  private static boolean has(UserModel currentUser, String requestedRight)
  {
    Set<UserRole> roles = currentUser.getRoles();
    for (UserRole role : roles)
    {
      if (hasRoleRightFor(UserRoles.of(role), requestedRight))
      {
        return true;
      }
    }
    return false;
  }

  private static boolean hasRoleRightFor(UserRoles role, String requestedRight)
  {
    if (role == null)
    {
      return false;
    }
    switch (role)
    {
    case NONE:
      return false;
    case USER:
      return userRoles.contains(requestedRight);
    case ADMIN:
      return adminRoles.contains(requestedRight);
    case MANAGER:
      return managerRoles.contains(requestedRight);
    case INVITE:
      return inviteRoles.contains(requestedRight);
    }
    return false;
  }

  public static boolean isUser(UserModel owner)
  {
    UserModel currentUser = KonferenzSession.get().getCurrentUser();
    if (currentUser == null || owner == null)
    {
      return false;
    }
    return currentUser.equals(owner);
  }

  public static boolean noUserLoggedIn()
  {
    return KonferenzSession.get().getCurrentUser() == null;
  }

  public static boolean isAdmin()
  {
    UserModel user = KonferenzSession.get().getCurrentUser();
    if (user == null || user.getRoles() == null)
    {
      return false;
    }
    for (UserRole role : user.getRoles())
    {
      UserRoles cur = UserRoles.of(role);
      if (cur.equals(UserRoles.ADMIN))
      {
        return true;
      }
    }
    return false;
  }

  public void setCurrentConference(ConferenceModel cModel)
  {
    _currentConference = cModel;
  }

  public ConferenceModel getCurrentConference()
  {
    if (_currentConference == null)
    {
      return facade.getActiveConference();
    }
    return _currentConference;
  }

  public boolean activeIsCurrent()
  {
    if (_currentConference == null)
    {
      return true;
    }
    ConferenceModel active = facade.getActiveConference();
    if (active == null)
    {
      return false;
    }
    return active.getId().equals(_currentConference.getId());
  }

  @SuppressWarnings("unused") // called by tests via reflection
  private static void setInternalMockForSession(KonferenzSession session)
  {
    _TestMockSession = session;
  }

  public static AppContext context()
  {
    return new AppContext()
    {
    };
  }
}
