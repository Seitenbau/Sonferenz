package de.bitnoise.sonferenz.service.v2.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import de.bitnoise.sonferenz.model.ConfigurationModel;
import de.bitnoise.sonferenz.model.StaticContentModel;


public interface StaticContentService
{
  String text(String key);
  
  String text(String key, String defaultValue);
  
  void storeText(String key, String text);

  Page<StaticContentModel> getAll(PageRequest request);

  StaticContentModel getById(Integer id);

  void saveText(String key, String textValue);
}
