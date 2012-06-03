package de.bitnoise.sonferenz.service.v2;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import de.bitnoise.testing.mockito.MockitoRule;

public class BaseTestClass
{
  @Rule
  public ExpectedException expectException = ExpectedException.none();

  @Rule
  public MockitoRule mockito = new MockitoRule();

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
  
  protected void expectException(Class<? extends Throwable> clazz, String message) {
    expectException.expect(clazz);
    expectException.expectMessage(message);
  }
}
