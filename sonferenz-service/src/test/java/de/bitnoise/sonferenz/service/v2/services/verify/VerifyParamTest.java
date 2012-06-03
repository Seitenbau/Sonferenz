package de.bitnoise.sonferenz.service.v2.services.verify;

import org.junit.Test;
import static org.fest.assertions.Assertions.*;

public class VerifyParamTest {

  @Test
  public void testCreateFor_Object() {
    VerifyObject result = VerifyParam.verify(new Object());
    assertThat(result).isInstanceOf(VerifyObject.class);
  }
  
}
