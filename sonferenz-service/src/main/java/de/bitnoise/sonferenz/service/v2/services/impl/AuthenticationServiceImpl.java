package de.bitnoise.sonferenz.service.v2.services.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.bitnoise.sonferenz.model.AuthMapping;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.model.UserRole;
import de.bitnoise.sonferenz.model.UserRoles;
import de.bitnoise.sonferenz.repo.AuthmappingRepository;
import de.bitnoise.sonferenz.repo.RoleRepository;
import de.bitnoise.sonferenz.repo.UserRepository;
import de.bitnoise.sonferenz.service.v2.Detach;
import de.bitnoise.sonferenz.service.v2.exceptions.GeneralConferenceException;
import de.bitnoise.sonferenz.service.v2.exceptions.NoRightsExcpetion;
import de.bitnoise.sonferenz.service.v2.security.ProviderType;
import de.bitnoise.sonferenz.service.v2.security.ProvidesEmail;
import de.bitnoise.sonferenz.service.v2.services.AuthenticationService;
import de.bitnoise.sonferenz.service.v2.services.MailService;
import de.bitnoise.sonferenz.service.v2.services.StaticContentService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService, ApplicationListener<ApplicationEvent>
{
			
  @Autowired
  AuthmappingRepository authRepo;
  
  @Autowired
  UserRepository userRepo;

  @Autowired
  RoleRepository rolesRepo;

  @Override
  @Transactional
  public UserModel getCurrentUser() throws GeneralConferenceException
  {
    try
    {
      return internGetCurrentUser();
    }
    catch (Throwable t)
    {
      throw new GeneralConferenceException("Unerwarteter Fehler aufgetreten",t);
    }
  }

  private UserModel internGetCurrentUser()
  {
    SecurityContext context = SecurityContextHolder.getContext();
    if (context == null)
    {
      return null;
    }
    Authentication authentication = context.getAuthentication();
    if (authentication == null)
    {
      return null;
    }
    String providerId = authentication.getName();
    String providerType = null;
    Object principal = authentication.getPrincipal();
    if (principal instanceof LdapUserDetails)
    {
      providerType = "ldap";
    }
    if (authentication instanceof ProviderType)
    {
      providerType = ((ProviderType) authentication).getProviderType();
    }
    if (principal instanceof ProviderType)
    {
      providerType = ((ProviderType) principal)
          .getProviderType();
    }
    if (providerType == null)
    {
      return null;
    }
    AuthMapping mapping = authRepo.findByAuthIdAndAuthType(providerId,
        providerType);
    if (mapping == null)
    {
      String email = null;
      if (principal instanceof ProvidesEmail)
      {
        ProvidesEmail  user = (ProvidesEmail) principal;
        email = user.getEmail();
      }
      UserModel neuerUser = neuenNutzerAnlegen(providerId, email);
      neuesAuthMappingAnlegen(neuerUser, providerId, providerType);
      return neuerUser;
    }
    return Detach.detach(mapping.getUser());
  }

  private void neuesAuthMappingAnlegen(UserModel neuerUser, String providerId,
      String providerType)
  {
    AuthMapping neuesMapping = new AuthMapping();
    neuesMapping.setAuthId(providerId);
    neuesMapping.setAuthType(providerType);
    neuesMapping.setUser(neuerUser);
    authRepo.save(neuesMapping);
  }

  private UserModel neuenNutzerAnlegen(String providerId, String email)
  {
    UserModel neuerUser = new UserModel();
    neuerUser.setName(providerId);
    neuerUser.setEmail(email);
    neuerUser.setCreatedAt(new Date());
	
    UserRole userRole = rolesRepo.findByName(UserRoles.USER.toString());
    UserRole inviteRole = rolesRepo.findByName(UserRoles.INVITE.toString());
    Set<UserRole> roles=new HashSet<UserRole>();
    roles.add(userRole);
    roles.add(inviteRole);
    neuerUser.setRoles(roles);
    
    userRepo.save(neuerUser);
    return neuerUser;
  }

  @Override
  @Transactional
  public UserModel getCurrentUserOrFail() {
	UserModel user = getCurrentUser();
	if(user==null) {
		throw new NoRightsExcpetion("You're not logged in");
	}
	return user;
  }
  
  @Override
  @Transactional
  public void onApplicationEvent(ApplicationEvent event) {
		if(event instanceof AuthenticationSuccessEvent) {
			AuthenticationSuccessEvent ase = (AuthenticationSuccessEvent) event;
			onSuccessfullLogin(ase.getAuthentication());
		}
  }

  protected void onSuccessfullLogin(Authentication authentication) {
	    if (authentication == null)
	    {
	      return ;
	    }
	    String providerId = authentication.getName();
	    String providerType = null;
	    Object principal = authentication.getPrincipal();
	    if (principal instanceof LdapUserDetails)
	    {
	      providerType = "ldap";
	    }
	    if (authentication instanceof ProviderType)
	    {
	      providerType = ((ProviderType) authentication).getProviderType();
	    }
	    if (principal instanceof ProviderType)
	    {
	      providerType = ((ProviderType) principal)
	          .getProviderType();
	    }
	    if (providerType == null)
	    {
	      return ;
	    }
	    UserModel benutzer;
	    AuthMapping mapping = authRepo.findByAuthIdAndAuthType(providerId, providerType);
	    if (mapping == null)
	    {
	      String email = null;
	      if (principal instanceof ProvidesEmail)
	      {
	        ProvidesEmail  user = (ProvidesEmail) principal;
	        email = user.getEmail();
	      }
	      benutzer = neuenNutzerAnlegen(providerId, email);
	      neuesAuthMappingAnlegen(benutzer, providerId, providerType);
	    } else {
	    	benutzer = mapping.getUser();
	    }
	    // Set last login date
	    if(benutzer.getCreatedAt() == null) {
	    	benutzer.setCreatedAt(new Date());
	    }
	    benutzer.setLastLogin(new Date());
	    userRepo.save(benutzer);
  }

  @Override
  public void queryUser(String usernameOrEmail) {
    if(usernameOrEmail == null || usernameOrEmail.isEmpty()) {
    	return;
    }
    UserModel found = null;
	if(usernameOrEmail.contains("@")) {
      found = userRepo.findByEmail(usernameOrEmail);
    } else {
      found = userRepo.findByName(usernameOrEmail);
    }
	if(found!=null) 
	{
	  sendInfoMailTo(found);	
	}
  }

  @Autowired
  StaticContentService texte;
  
  @Autowired
  MailService mailer;
  
  void sendInfoMailTo(UserModel found) 
  {
	  String mail = found.getEmail();
	  if(mail==null || !mail.contains("@")) {
		  return;
	  }
	  SimpleMailMessage template;
	  template = new SimpleMailMessage();
	  template.setSubject(texte.text("action.query.username.subject"));
	  String body = texte.text("action.query.username.body");
	  body=body.replace("${username}", found.getName());
	  template.setText(body);
	  template.setTo(mail);
	  SimpleMailMessage message = new SimpleMailMessage(template);
	  mailer.sendMessage(message);
  }
}
