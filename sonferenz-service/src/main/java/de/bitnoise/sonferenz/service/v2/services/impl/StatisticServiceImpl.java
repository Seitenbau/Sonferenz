package de.bitnoise.sonferenz.service.v2.services.impl;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.bitnoise.sonferenz.model.StatisticModel;
import de.bitnoise.sonferenz.model.StatisticRecordModel;
import de.bitnoise.sonferenz.repo.StatisticRecordRepository;
import de.bitnoise.sonferenz.repo.StatisticRepository;
import de.bitnoise.sonferenz.service.v2.services.StatisticService;

@Service
public class StatisticServiceImpl implements StatisticService
{
  @Autowired
  StatisticRepository repo;
  
  @Autowired
  StatisticRecordRepository rrepo;

  @Override
  @Transactional
  public void incrementHit(String objectId, ResourceEvent span)
  {
    StatisticModel entry = repo.findByTypeAndName("video", objectId);
    if (entry == null)
    {
      entry = new StatisticModel();
      entry.setName(objectId);
      entry.setType("video");
      entry.setRecords(new HashMap<String, StatisticRecordModel>());
      repo.saveAndFlush(entry);
    }
    Map<String, StatisticRecordModel> records = entry.getRecords();
    if(records == null) {
      records = new HashMap<String, StatisticRecordModel>();
      entry.setRecords(records);
      repo.save(entry);
    }
    StatisticRecordModel item = records.get(span.getKey());
    if (item == null)
    {
      item = new StatisticRecordModel();
      item.setBlock(span.getKey());
      item.setCounter(1L);
      records.put(span.getKey(),item);
    }
    else
    {
      item.setCounter(item.getCounter() + 1);
    }
    rrepo.save(item);
    repo.save(entry);
  }

}
