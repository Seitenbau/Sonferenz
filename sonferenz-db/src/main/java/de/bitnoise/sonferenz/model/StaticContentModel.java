package de.bitnoise.sonferenz.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;


@Entity
@Data
@Table 
@EqualsAndHashCode(of =
  { "id" })
public class StaticContentModel implements DoInterface<Integer>
{
  @Id
  @GeneratedValue
  Integer id;
  
  @Column
  @Index(name="name")
  String name;
  
  @Column
  Boolean allowHtml;

  @Column
  @Type(type = "text")
  String html;
  
}
