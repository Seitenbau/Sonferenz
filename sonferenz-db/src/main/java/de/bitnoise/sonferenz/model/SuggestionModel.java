package de.bitnoise.sonferenz.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Type;


@Entity
@Data
@Table 
@EqualsAndHashCode(of =
  { "id" })
public class SuggestionModel extends ModelBase implements DoInterface<Integer>
{
  @Id
  @GeneratedValue
  Integer id;

  @OneToOne
  UserModel owner;

  @Column 
  String title;

  @Column
  @Type(type = "text")
  String description;
  
  @Column
  Integer likes;
  
  @OneToMany(targetEntity=LikeModel.class,mappedBy="whish",
      fetch=FetchType.LAZY,cascade=CascadeType.REMOVE)
  Set<LikeModel> likeRef;
}
