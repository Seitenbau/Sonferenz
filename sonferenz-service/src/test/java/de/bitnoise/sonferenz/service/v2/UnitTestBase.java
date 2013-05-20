package de.bitnoise.sonferenz.service.v2;

import org.junit.Rule;
import org.junit.rules.ExpectedException;

import de.bitnoise.testing.mockito.MockitoRule;

public class UnitTestBase
{

  @Rule
  public ExpectedException expectException = ExpectedException.none();
  @Rule
  public MockitoRule mockito = new MockitoRule();

  public UnitTestBase()
  {
    super();
  }

  protected void expectedException(Class<? extends Throwable> clazz, String message)
  {
    expectException.expect(clazz);
    expectException.expectMessage(message);
  }

}