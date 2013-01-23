package de.bitnoise.sonferenz.service.v2.services.impl;

import static org.mockito.Mockito.*;
import static org.fest.assertions.Assertions.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import de.bitnoise.sonferenz.model.ActionModel;
import de.bitnoise.sonferenz.repo.ActionRepository;
import de.bitnoise.sonferenz.service.v2.BaseTestClass;
import de.bitnoise.sonferenz.service.v2.actions.Aktion;
import de.bitnoise.sonferenz.service.v2.actions.KonferenzAction;

public class ActionServiceImplTest extends BaseTestClass
{
  ActionServiceImpl sut = new ActionServiceImpl();

  @Mock
  KonferenzAction aktionImpl;

  @Mock
  ActionRepository repo;

  String token = "a98a6afd69afd45fda48fd73fd";

  @Mock
  ActionModel actionModel;


  @Before
  public void before()
  {
    // add a fake action
    Map<String, KonferenzAction> oneAction = new HashMap<String, KonferenzAction>();
    oneAction.put("theBeanName", aktionImpl);
    when(aktionImpl.getActionName()).thenReturn("eineAktion");

    // fake db row
    ActionModel row = new ActionModel();
    row.setAction("eineAktion");
    row.setActive(true);
    row.setData(null);
    row.setExpiry(in5Minuten.toDate());
    row.setId(42);
    row.setUsed(0);
    row.setToken(token);
    
    when(repo.findByActionAndToken("eineAktion", token))
        .thenReturn(row);

    // setup sut
    sut.repo = repo;
    sut.initActions(oneAction);
  }

  @Test
  public void loadAction()
  {
    when(actionModel.getExpiry()).thenReturn(new Date());
    Aktion result = sut.loadAction("eineAktion", token);
    assertThat(result).isNotNull();
  }

  @Test
  @Ignore("2 implement")
  public void getUserActions()
  {

  }

  @Test
  @Ignore("2 implement")
  public void createAction()
  {

  }

  // *************** execute ***************

  @Test(expected = NullPointerException.class)
  public void execute_Null()
  {
    sut.execute(null);
  }
}
