package de.bitnoise.sonferenz.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
// @Table
@EqualsAndHashCode(of = { "id" })
public class FileResourceModel extends ModelBase implements DoInterface<Integer>
{
  @Id
  @GeneratedValue
  Integer id;

  @Column
  String name;
  
  @Column
  String originalName;

  @Column
  @Temporal(TemporalType.TIMESTAMP)
  Date createdAt;

  @Column
  @Temporal(TemporalType.TIMESTAMP)
  Date lastAccess;

  @OneToOne
  UserModel owner;

  @Column
  String md5sum;
  
  @Column
  Long size;

  @Column
  @Lob
  byte[] content;

}
