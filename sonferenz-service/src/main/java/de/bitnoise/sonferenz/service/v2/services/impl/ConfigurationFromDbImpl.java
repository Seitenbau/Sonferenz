package de.bitnoise.sonferenz.service.v2.services.impl;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.hibernate.annotations.OptimisticLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.eventbus.Subscribe;

import de.bitnoise.sonferenz.model.ConfigurationModel;
import de.bitnoise.sonferenz.repo.ConfigurationRepository;
import de.bitnoise.sonferenz.service.v2.events.ApplicationStarted;
import de.bitnoise.sonferenz.service.v2.events.ConfigReload;
import de.bitnoise.sonferenz.service.v2.exceptions.ConfigurationError;
import de.bitnoise.sonferenz.service.v2.services.ConfigurationService;
import de.bitnoise.sonferenz.service.v2.services.Eventing;

@Service
public class ConfigurationFromDbImpl implements ConfigurationService
{
  @Autowired
  ConfigurationRepository repo;

  @Override
  @Transactional(readOnly = true)
  public String getStringValue(String... keysToSearch)
  {
    String value = getNextValue(keysToSearch);
    if (value == null)
    {
      throw new ConfigurationError("The Config key was not accessible "
          + Arrays.toString(keysToSearch));
    }
    return value;
  }

  @Subscribe
  public void onApplicationStarted(ApplicationStarted event)
  {
    events.post(new ConfigReload());
  }

  @Autowired
  Eventing events;

  @Override
  public Page<ConfigurationModel> getAllConfigurations(PageRequest request)
  {
    return repo.findAll(request);
  }

  protected String getNextValue(String... keysToSearch)
  {
    for (String key : keysToSearch)
    {
      ConfigurationModel row = repo.findByName(key);
      if (row == null)
      {
        continue;
      }
      String raw = row.getValueString();
      return raw;
    }
    return null;
  }

  @Override
  public Integer getIntegerValue(String... keysToSearch)
  {
    String value = getNextValue(keysToSearch);
    if (value == null)
    {
      throw new ConfigurationError("The Config key was not accessible "
          + Arrays.toString(keysToSearch));
    }
    try
    {
      return Integer.valueOf(value);
    }
    catch (NumberFormatException nfe)
    {
      throw new ConfigurationError("The Config key was not a integer "
          + value);
    }
  }

  @Override
  public Integer getIntegerValue(int defaultValue, String... keysToSearch)
  {
    String value = getNextValue(keysToSearch);
    if (value == null)
    {
      return defaultValue;
    }
    try
    {
      return Integer.valueOf(value);
    }
    catch (NumberFormatException nfe)
    {
      return defaultValue;
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Boolean isAvaiable(String... keysToSearch)
  {
    String value = getNextValue(keysToSearch);
    return value != null;
  }

  @Override
  public void initValue(String key, Integer value)
  {
    doInitValue(key, value);
  }

  @Override
  public void initValue(String key, String value)
  {
    doInitValue(key, value);
  }

  private void doInitValue(String key, Object value)
  {
    if (repo.findByName(key) == null)
    {
      ConfigurationModel config = new ConfigurationModel();
      config.setDataType(value.getClass().getSimpleName());
      config.setName(key);
      config.setValueString(value.toString());
      repo.save(config);

      events.post(new ConfigReload());
    }
  }

  @Override
  public ConfigurationModel getById(Integer id)
  {
    ConfigurationModel found = repo.findOne(id);
    return found;
  }

  @Override
  public void saveStringValue(String key, String value)
  {
    ConfigurationModel found = repo.findByName(key);
    if (found == null)
    {
      found = new ConfigurationModel();
      found.setName(key);
    }
    found.setValueString(value);
    repo.save(found);

    events.post(new ConfigReload());
  }

  @Override
  @Transactional(readOnly = true)
  public String getStringValueOr(String defaultValue, String key)
  {
    String value = getNextValue(key);
    if (value == null)
    {
      return defaultValue;
    }
    return value;
  }

  @Override
  public void saveIntegerValue(String key, int value) {
    ConfigurationModel found = repo.findByName(key);
    if (found == null)
    {
      found = new ConfigurationModel();
      found.setName(key);
    }
    found.setValueString(Integer.toString(value));
    repo.save(found);

    events.post(new ConfigReload());
  }
}
