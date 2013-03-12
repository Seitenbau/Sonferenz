package de.bitnoise.sonferenz.service.v2.actions.impl;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.Subscribe;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import de.bitnoise.sonferenz.model.AuthMapping;
import de.bitnoise.sonferenz.repo.ActionRepository;
import de.bitnoise.sonferenz.repo.AuthmappingRepository;
import de.bitnoise.sonferenz.repo.UserRepository;
import de.bitnoise.sonferenz.service.v2.actions.ActionResult;
import de.bitnoise.sonferenz.service.v2.actions.ActionState;
import de.bitnoise.sonferenz.service.v2.actions.DeleteOnSuccess;
import de.bitnoise.sonferenz.service.v2.actions.IncrementUseageCount;
import de.bitnoise.sonferenz.service.v2.actions.KonferenzAction;
import de.bitnoise.sonferenz.service.v2.events.ConfigReload;
import de.bitnoise.sonferenz.service.v2.exceptions.ValidationException;
import de.bitnoise.sonferenz.service.v2.services.ActionService;
import de.bitnoise.sonferenz.service.v2.services.AuthenticationService;
import de.bitnoise.sonferenz.service.v2.services.ConfigurationService;
import de.bitnoise.sonferenz.service.v2.services.MailService;
import de.bitnoise.sonferenz.service.v2.services.StaticContentService;
import de.bitnoise.sonferenz.service.v2.services.UserService;
import de.bitnoise.sonferenz.service.v2.services.idp.IdpService;

@Service
public class ChangePasswordActionImpl implements KonferenzAction
{
  SimpleMailMessage template;

  @Autowired
  AuthenticationService authService;

  @Autowired
  ConfigurationService config;
  
  @Autowired
  IdpService idps;

  @Override
  public String getActionName()
  {
    return "changePassword";
  }

  @Override
  public boolean execute(ActionState data)
  {
    if (data instanceof ChangePasswordOfUser)
    {
      doExecute((ChangePasswordOfUser) data);
      return true;
    }
    return false;
  }

  @Subscribe
  public void onConfigReload(ConfigReload event)
  {
    template = new SimpleMailMessage();
    template.setSubject(
        texte.text("action.changepassword.mail.subject"));
  }

  @Autowired
  ActionService actionService;

  public void createNewUserToken(AuthMapping id, String newPassword) {
	  String email = id.getUser().getEmail();
//    UserModel foundMail = userRepo.findByEmail(mail);
//    if (foundMail != null)
//    {
//      throw new ValidationException("eMail allready inuse");
//    }
//    UserModel foundName = userRepo.findByName(user);
//    if (foundName != null)
//    {
//      throw new ValidationException("Username allready inuse");
//    }
//    AuthMapping foundLogin = authRepo.findByAuthIdAndAuthType(user,
//        LocalIdp.IDP_NAME);
//    if (foundLogin != null)
//    {
//      throw new ValidationException("Login Name allready inuse");
//    }
//    String body = forceBody;
//    if (body == null || !body.contains("${link}"))
//    {
      String body = texte.text("action.changepassword.mail.body");
//      if (body == null)
//      {
//        throw new ValidationException(
//            "Missing Mail Body : key='action.subscribe.mail.body' ");
//      }
//    }
//    if (subject != null)
//    {
//      template.setSubject(subject);
//    }
//    else
//    {
      String subject = texte.text("action.changepassword.mail.subject");
//    }
    ChangePasswordOfUser newUser = new ChangePasswordOfUser();
    newUser.setNewPassword(newPassword);
    newUser.setProvider(id.getAuthType());
    newUser.setAuthId(id.getAuthId());
//    newUser.setTokenId(tokenId);
//    newUser.setLoginName(user);
//    newUser.setMail(mail);
//    if (provider == null || 
//        ( provider.equals(CrowdIdp.IDP_NAME) && provider.equals(LocalIdp.IDP_NAME) ))
//    {
//      newUser.setProvider(CrowdIdp.IDP_NAME);
//    } else {
//      newUser.setProvider(provider);
//    }

    ActionResult result = actionService.createAction(this, newUser);
    if (result.wasSuccessfull())
    {
      SimpleMailMessage message = new SimpleMailMessage(template);
      // message.setTo(email);
      message.setTo("rainer.weinhold@seitenbau.com");
//      body = body.replace("${link}", ActionResult.ACTION_URL);
      message.setText(body.toString());
      message.setSubject(subject);
      mailer.sendMessage(result.getContentReplacer(), message);
    }
  }

  @Autowired
  StaticContentService texte;

  @Autowired
  UserRepository userRepo;

  @Autowired
  AuthmappingRepository authRepo;

  @Autowired
  UserService userService;

  @Autowired
  ActionRepository repo;

  @Autowired
  MailService mailer;

  void doExecute(ChangePasswordOfUser data)
  {
    AuthMapping map = authRepo.findByAuthIdAndAuthType(data.getAuthId(), data.getProvider());
    if (map == null)
    {
      throw new ValidationException("map allready in use");
    }
    idps.setPassword(data.getProvider(), data.getAuthId(), data.getNewPassword());
  }

  public String createToken()
  {
    return UUID.randomUUID().toString();
  }

  @Override
  public Class[] getModelClasses()
  {
    return new Class[] {ChangePasswordOfUser.class};
  }

  @XStreamAlias("ChangePasswordOfUser")
  public static class ChangePasswordOfUser implements ActionState,
  DeleteOnSuccess
  {
    Integer tokenId;
    
    String authId;
    
	String provider;
    
    String newPassword;

    public void setTokenId(Integer tokenId)
    {
      this.tokenId = tokenId;
    }
    
	@Override
    public List<Integer> getTokensToDelete()
    {
      return Arrays.asList(tokenId);
    }

    public String getActionName()
    {
      return "changePassword";
    }

    @Override
    public String getTitle()
    {
      return "Change Password of user ";
    }
    
    public String getAuthId() {
		return authId;
	}

	public void setAuthId(String username) {
		this.authId = username;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

  }


}
