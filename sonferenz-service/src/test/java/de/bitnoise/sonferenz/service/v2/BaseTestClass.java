package de.bitnoise.sonferenz.service.v2;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDateTime;


public class BaseTestClass extends UnitTestBase
{
  protected LocalDateTime jetzt = LocalDateTime.now();

  protected LocalDateTime in5Minuten = LocalDateTime.now().plusMinutes(5);

  List<Object> strictMocks = new ArrayList<Object>();

  protected <MOCK> MOCK strictMock(Class<MOCK> clazz)
  {
    MOCK obj = mock(clazz);
    strictMocks.add(obj);
    return obj;
  }

  protected void replay(Object... mocks)
  {
    for (Object obj : strictMocks)
    {
      verifyNoMoreInteractions(obj);
    }
    if (mocks != null && mocks.length > 0)
    {
      verifyNoMoreInteractions(mocks);
    }
  }
}
