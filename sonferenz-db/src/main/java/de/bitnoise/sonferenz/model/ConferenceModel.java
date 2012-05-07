package de.bitnoise.sonferenz.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Data
@EqualsAndHashCode(of={"id"})
public class ConferenceModel extends ModelBase implements DoInterface<Integer>
{
  @Id
  @GeneratedValue
  Integer id;

  @Column
  String shortName;
  
  @Column
  String title;
  
  @Column
  String description;
  
  @Column
  Boolean active;
  
  @Column
  Integer votesPerUser;
  
  @Column
  Integer votesRecommend;

  @Enumerated(EnumType.STRING)
  ConferenceState state;


}
