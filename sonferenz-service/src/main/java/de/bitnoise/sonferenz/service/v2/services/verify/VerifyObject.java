package de.bitnoise.sonferenz.service.v2.services.verify;


public class VerifyObject {

  protected StringBuffer _description = new StringBuffer();
  protected Object _current;

  public VerifyObject(Object parameter) {
    _current = parameter;
  }

  public VerifyObject as(String text) {
    addDescription(text);
    return this;
  }

  public void addDescription(String text) {
    _description.append(text);
  }

  public void notNull() {
    if (_current == null) {
      error(" was null");
    }
  }

  protected void error(String postifx) {
    addDescription(postifx);
    throw new IllegalArgumentException(_description.toString());
  }

}
