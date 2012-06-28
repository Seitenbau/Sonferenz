package de.bitnoise.sonferenz.service.v2.services.impl;

import de.bitnoise.sonferenz.model.FileResourceModel;

public interface ResourceService {
  FileResourceModel storeResource(String filename, byte[] data, byte[] md5, Long size);
}
