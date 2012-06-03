package de.bitnoise.sonferenz.service.v2.services.verify;

import org.junit.Test;

import de.bitnoise.sonferenz.service.v2.BaseTestClass;

public class VerifyObjectTest extends BaseTestClass {

  @Test
  public void notNull_withNull() {
    // prepare
    VerifyObject sut = new VerifyObject(null);
    // verify
    expectException(IllegalArgumentException.class, "was null");
    // execute
    sut.isNotNull();
  }

  @Test
  public void notNull_withObject() {
    // prepare
    VerifyObject sut = new VerifyObject(new Object());
    // execute
    sut.isNotNull();
    // verify : nothing = green
  }
  
  @Test
  public void notNull_withNullAndDescription() {
    // prepare
    VerifyObject sut = new VerifyObject(null);
    // verify
    expectException(IllegalArgumentException.class, "parameter 1 was null");
    // execute
    sut.as("parameter 1");
    sut.isNotNull();
  }

}
