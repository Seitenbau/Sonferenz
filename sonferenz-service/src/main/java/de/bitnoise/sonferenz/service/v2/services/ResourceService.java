package de.bitnoise.sonferenz.service.v2.services;

import de.bitnoise.sonferenz.model.FileResourceModel;
import de.bitnoise.sonferenz.model.ResourceModel;

public interface ResourceService {
  
  FileResourceModel storeResource(String filename, byte[] data, byte[] md5, Long size);

  void registerPlay(ResourceModel video);
}
