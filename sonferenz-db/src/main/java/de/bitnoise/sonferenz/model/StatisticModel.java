package de.bitnoise.sonferenz.model;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table
public class StatisticModel implements DoInterface<Integer>
{
  @Id
  @GeneratedValue
  Integer id;

  @Column
  String type;
  
  @Column
  String name;

  @OneToMany
  Map<String,StatisticRecordModel> records;

}
