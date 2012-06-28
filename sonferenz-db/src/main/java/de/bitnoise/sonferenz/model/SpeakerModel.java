package de.bitnoise.sonferenz.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.hibernate.annotations.Type;


@Entity
@Data
//@Table 
@EqualsAndHashCode(of={"id"})
public class SpeakerModel extends ModelBase implements DoInterface<Integer>
{
  @Id
  @GeneratedValue
  Integer id;

  @Column
  String name;
  
  @Column
  @Temporal(TemporalType.TIMESTAMP)
  Date createdAt;
  
  @OneToOne
  UserModel contact;
  
  @OneToOne
  FileResourceModel picture;
  
  @Column
  @Type(type = "text")
  String description;

  @Override
  public String toString()
  {
    return getName();
  }

}
