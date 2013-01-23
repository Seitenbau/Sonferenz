package de.bitnoise.sonferenz.service.v2.services.idp.provider.crowd;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "group")
public class CrowdGroup
{

  private String name;

  @XmlAttribute
  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }
}
