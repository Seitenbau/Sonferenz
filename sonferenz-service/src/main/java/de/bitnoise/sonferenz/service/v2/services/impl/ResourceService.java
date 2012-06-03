package de.bitnoise.sonferenz.service.v2.services.impl;

import de.bitnoise.sonferenz.model.ResourceModel;

public interface ResourceService {
  ResourceModel storeResource(String filename, byte[] data, byte[] md5, Long size);
}
