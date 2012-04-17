package de.bitnoise.sonferenz.service.v2.actions.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.Subscribe;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import de.bitnoise.sonferenz.model.AuthMapping;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.model.UserRoles;
import de.bitnoise.sonferenz.repo.ActionRepository;
import de.bitnoise.sonferenz.repo.AuthmappingRepository;
import de.bitnoise.sonferenz.repo.UserRepository;
import de.bitnoise.sonferenz.service.v2.actions.ActionResult;
import de.bitnoise.sonferenz.service.v2.actions.ActionState;
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
import de.bitnoise.sonferenz.service.v2.services.idp.provider.local.LocalIdp;

@Service
public class SubscribeActionImpl implements KonferenzAction
{

  SimpleMailMessage template;

  @Autowired
  AuthenticationService authService;

  @Autowired
  VerifyMailActionImpl actionVerify;

  @Autowired
  ConfigurationService config;

  @Override
  public String getActionName()
  {
    return "subscribe";
  }

  @Override
  public boolean execute(ActionState data)
  {
    if (data instanceof ActionCreateUser)
    {
      doExecute((ActionCreateUser) data);
      return true;
    }
    return false;
  }

  @Subscribe
  public void onConfigReload(ConfigReload event)
  {
    template = new SimpleMailMessage();
    template.setSubject(
        texte.text("action.subscribe.mail.subject"));
  }

  @Autowired
  ActionService actionService;

  public void createNewUserToken(String user, String mail, String forceBody,
      String subject)
  {
    UserModel foundMail = userRepo.findByEmail(mail);
    if (foundMail != null)
    {
      throw new ValidationException("eMail allready inuse");
    }
    UserModel foundName = userRepo.findByName(user);
    if (foundName != null)
    {
      throw new ValidationException("Username allready inuse");
    }
    AuthMapping foundLogin = authRepo.findByAuthIdAndAuthType(user,
        LocalIdp.IDP_NAME);
    if (foundLogin != null)
    {
      throw new ValidationException("Login Name allready inuse");
    }
    String body = forceBody;
    if (body == null || !body.contains("${link}"))
    {
      body = texte.text("action.subscribe.mail.body");
      if (body == null)
      {
        throw new ValidationException(
            "Missing Mail Body : key='action.subscribe.mail.body' ");
      }
    }
    if (subject != null)
    {
      template.setSubject(subject);
    }
    else
    {
      template.setSubject(
          texte.text("action.subscribe.mail.subject"));
    }
    ActionCreateUser newUser = new ActionCreateUser();
    newUser.setLoginName(user);
    newUser.setMail(mail);

    ActionResult result = actionService.createAction(this, newUser);
    if (result.wasSuccessfull())
    {
      SimpleMailMessage message = new SimpleMailMessage(template);
      message.setTo(mail);
      body = body.replace("${link}", ActionResult.ACTION_URL);
      message.setText(body.toString());
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

  void doExecute(ActionCreateUser data)
  {
    UserModel foundMail = userRepo.findByEmail(data.getEMail());
    if (foundMail != null)
    {
      throw new ValidationException("eMail allready inuse");
    }
    UserModel foundName = userRepo.findByName(data.getUserName());
    if (foundName != null)
    {
      throw new ValidationException("Username allready inuse");
    }
    AuthMapping foundLogin = authRepo.findByAuthIdAndAuthType(
        data.getLoginName(), LocalIdp.IDP_NAME);
    if (foundLogin != null)
    {
      throw new ValidationException("Login Name allready inuse");
    }

    Collection<UserRoles> newRoles = new ArrayList<UserRoles>();
    newRoles.add(UserRoles.USER);

    UserModel user = userService.createIdentity(data.getProvider(),
        data.getLoginName(), data.getUserName(),
        data.getPassword(), data.getEMail(), newRoles);

    // actionVerify.createAction(user, data.getEMail());
  }

  public String createToken()
  {
    return UUID.randomUUID().toString();
  }

  @Override
  public Class[] getModelClasses()
  {
    return new Class[] {ActionCreateUser.class};
  }

  @XStreamAlias("ActionCreateUser")
  public static class ActionCreateUser implements ActionState,
      IncrementUseageCount
  {

    String loginName;

    public String getLoginName()
    {
      return loginName;
    }

    public String getUserName()
    {
      return userName;
    }

    public String getPassword()
    {
      return password;
    }

    String userName;

    String password;

    List<String> groups;

    String mail;

    public void setUserName(String userName)
    {
      this.userName = userName;
    }

    public void setMail(String mail)
    {
      this.mail = mail;
    }

    public void setTokenId(Integer tokenId)
    {
      this.tokenId = tokenId;
    }

    Integer tokenId;

    public void setLoginName(String value)
    {
      loginName = value;
    }

    public void setPassword(String value)
    {
      password = value;
    }

    public void addGroupo(String groupName)
    {
      getGroups().add(groupName);
    }

    public List<String> getGroups()
    {
      if (groups == null)
      {
        groups = new ArrayList<String>();
      }
      return groups;
    }

    public String getEMail()
    {
      return mail;
    }

    @Override
    public List<Integer> getTokensToIncrementUseage()
    {
      return Arrays.asList(tokenId);
    }

    public String getActionName()
    {
      return "subscribe";
    }

    private String provider;

    public String getProvider()
    {
      return provider;
    }

    public void setProvider(String provider)
    {
      this.provider = provider;
    }

	@Override
    public String getTitle() {
	    return "Invite of user : '" + mail + "'";
    }

  }

}
