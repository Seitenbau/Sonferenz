package de.bitnoise.sonferenz.service.v2.services.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.jasypt.digest.StringDigester;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thoughtworks.xstream.XStream;

import de.bitnoise.sonferenz.model.ActionModel;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.repo.ActionRepository;
import de.bitnoise.sonferenz.service.v2.actions.ActionResult;
import de.bitnoise.sonferenz.service.v2.actions.ActionState;
import de.bitnoise.sonferenz.service.v2.actions.Aktion;
import de.bitnoise.sonferenz.service.v2.actions.ContentReplacement;
import de.bitnoise.sonferenz.service.v2.actions.DeleteOnSuccess;
import de.bitnoise.sonferenz.service.v2.actions.IncrementUseageCount;
import de.bitnoise.sonferenz.service.v2.actions.InvalidateOnSuccess;
import de.bitnoise.sonferenz.service.v2.actions.KonferenzAction;
import de.bitnoise.sonferenz.service.v2.actions.impl.SubscribeActionImpl.ActionCreateUser;
import de.bitnoise.sonferenz.service.v2.exceptions.ValidationException;
import de.bitnoise.sonferenz.service.v2.services.ActionService;
import de.bitnoise.sonferenz.service.v2.services.AuthenticationService;
import de.bitnoise.sonferenz.service.v2.services.ConfigurationService;

@Service
public class ActionServiceImpl implements ActionService
{
  @Autowired
  ConfigurationService config;

  @Autowired
  ApplicationContext spring;

  @Autowired
  AuthenticationService authService;

  @Autowired
  ActionRepository actionRepo;

  Map<String, KonferenzAction> actionMap;

  String baseUrl2;

  String getBaseUrl()
  {
    if (baseUrl2 == null)
    {
      baseUrl2 = config.getStringValue("baseUrl");
    }
    return baseUrl2;
  }

  @PostConstruct
  public void initActions()
  {
    Map<String, KonferenzAction> allActions = spring
        .getBeansOfType(KonferenzAction.class);
    initActions(allActions);

  }

  public void initActions(Map<String, KonferenzAction> allActions)
  {
    actionMap = new HashMap<String, KonferenzAction>();
    xs = new XStream();
    for (Entry<String, KonferenzAction> entry : allActions.entrySet())
    {
      KonferenzAction bean = entry.getValue();
      actionMap.put(bean.getActionName(), bean);

      // xs.autodetectAnnotations(true); Not Thread Safe
      xs.processAnnotations(bean.getModelClasses());
    }
  }

  @Autowired
  StringDigester digester;

  @Override
  @Transactional(readOnly = true)
  public Aktion loadAction(String action, String token)
  {
    if (actionMap.get(action) == null)
    {
      throw new ValidationException("Not a valid action");
    }

    ActionModel row = repo.findByActionAndToken(action, token);
    Aktion aktion = toAktion(row);
    if (aktion == null)
    {
      return null;
    }
    if (!row.getAction().equals(action))
    {
      return null;
    }
    if (!row.getToken().equals(token))
    {
      return null;
    }
    if (!row.getActive())
    {
      return null;
    }
    if (row.getUsed() > 0)
    {
      return null;
    }
    Date expires = row.getExpiry();
    if (new LocalDateTime(expires).isBefore(LocalDateTime.now()))
    {
      return null;
    }

    return aktion;
  }

  @Override
  @Transactional(readOnly=true)
  public Page<ActionModel> getUserActions(PageRequest request, UserModel user)
  {
    return repo.findByCreator(user, request);
  }

  @Autowired
  ActionRepository repo;

  private XStream xs;

  @Override
  @Transactional(rollbackFor = Throwable.class)
  public void execute(ActionState data)
  {
    KonferenzAction action = actionMap.get(data.getActionName());
    if (action == null)
    {
      throw new ValidationException("Unkown Action");
    }
    boolean result = action.execute(data);
    processActionTable(data, result);
  }

  void processActionTable(ActionState data, boolean wasSuccessfull)
  {
    if (data instanceof IncrementUseageCount)
    {
      List<Integer> tokens = ((IncrementUseageCount) data)
          .getTokensToIncrementUseage();
      incrementUsage(tokens);
    }
    if (data instanceof InvalidateOnSuccess)
    {
      List<Integer> tokens = ((InvalidateOnSuccess) data)
          .getTokensToInvalidate();
      invalidateToken(tokens);
    }
    if (data instanceof DeleteOnSuccess)
    {
    	List<Integer> tokens = ((DeleteOnSuccess) data)
    			.getTokensToDelete();
    	deleteTokens(tokens);
    }
  }

  protected void invalidateToken(List<Integer> tokens)
  {
    if (tokens != null)
    {
      for (Integer id : tokens)
      {
        ActionModel token = repo.findOne(id);
        token.setActive(false);
        repo.save(token);
      }
    }
  }
  
  protected void deleteTokens(List<Integer> tokens)
  {
	  if (tokens != null)
	  {
		  for (Integer id : tokens)
		  {
			  repo.delete(id);
		  }
	  }
  }

  protected void incrementUsage(List<Integer> tokens)
  {
    if (tokens != null)
    {
      for (Integer id : tokens)
      {
        ActionModel token = repo.findOne(id);
        if (token.getUsed() == null)
        {
          token.setUsed(1);
        }
        else
        {
          token.setUsed(token.getUsed() + 1);
        }
        repo.save(token);
      }
    }
  }

  @Override
  public ActionResult createAction(KonferenzAction action, ActionState data)
  {
    ActionModel entity = new ActionModel();
    entity.setAction(data.getActionName());
    String token = createToken();
    entity.setToken(token);
    entity.setActive(true);
    entity.setCreator(authService.getCurrentUser());
    entity.setTitle(data.getTitle());
    int maxValidity = 21 * 24 * 60; // 21 Tage
    DateTime result = DateTime.now().plus(Period.minutes(maxValidity));
    entity.setExpiry(result.toDate());
    entity.setUsed(0);
    XStream xs = getXStream();
    entity.setData(xs.toXML(data));
    repo.save(entity);

    return new ActionResultImpl(getBaseUrl(), data.getActionName(), token);
  }

  public static class ActionResultImpl implements ActionResult
  {
    ContentReplacement replacer;

    public ActionResultImpl(final String baseUrl, final String actionName,
        final String token)
    {
      replacer = new ContentReplacement()
      {
        @Override
        public String process(String input)
        {
          String result = input.replace("${url.base}", baseUrl);
          result = result.replace("${url.action}", baseUrl + "/action/"
              + actionName
              + "/token/" + token);
          return result;
        }
      };
    }

    @Override
    public boolean wasSuccessfull()
    {
      return true;
    }

    @Override
    public ContentReplacement getContentReplacer()
    {
      return replacer;
    }
  }

  public String createToken()
  {
    return UUID.randomUUID().toString();
  }

  public String asString(ActionCreateUser newUser)
  {
    XStream xs = getXStream();
    return xs.toXML(newUser);
  }

  protected XStream getXStream()
  {
    return xs;
  }

  public Aktion toAktion(ActionModel row)
  {
    if (row == null)
    {
      return null;
    }
    if (row.getExpiry() == null)
    {
      return null;
    }
    XStream xs = getXStream();
    ActionState data = null;
    if (row.getData() != null)
    {
      data = (ActionState) xs.fromXML(row.getData());
    }
    Aktion a = new Aktion(row.getId(), row.getAction(), row.getToken(), data);
    return a;
  }

  @Override
  @Transactional(readOnly=true)
  public Page<ActionModel> getAllUserActions(PageRequest request, UserModel user) {
	return repo.findAll(request);
  }
}
