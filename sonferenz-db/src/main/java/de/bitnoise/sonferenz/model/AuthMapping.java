package de.bitnoise.sonferenz.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity 
@Data
@EqualsAndHashCode(of={"id"})
@Table(
  uniqueConstraints =
  { @UniqueConstraint(columnNames =
    { "authType", "authId" }) })
public class AuthMapping extends ModelBase implements DoInterface<Integer>
{

  @Override
  public boolean equals(Object obj)
  {
    return super.equals(obj);
  }

  @Override
  public String toString()
  {
    return Integer.toString(getId());
  }

  @Id
  @GeneratedValue
  Integer id;

  @OneToOne(fetch = FetchType.EAGER)
  UserModel user;

  @Basic
  @Column(length=100)
  String authType;

  @Basic
  @Column(length=100)
  String authId;
}
