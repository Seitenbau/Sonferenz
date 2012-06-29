package de.bitnoise.sonferenz.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table
public class StatisticRecordModel implements DoInterface<Integer>
{
  @Id
  @GeneratedValue
  Integer id;
  
  @Column
  String block;
  
  @Column
  Long counter;
}
