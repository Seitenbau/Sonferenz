package de.bitnoise.sonferenz.service.v2.services.impl;

import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.bitnoise.sonferenz.model.ResourceModel;
import de.bitnoise.sonferenz.repo.ResourceRepository;
import de.bitnoise.sonferenz.service.v2.services.AuthenticationService;
import static de.bitnoise.sonferenz.service.v2.services.verify.VerifyParam.*;

@Service
public class ResourceServiceImpl implements ResourceService {

  @Autowired
  ResourceRepository repo;

  @Autowired
  AuthenticationService auth;

  @Override
  public ResourceModel storeResource(String filename, byte[] data, byte[] md5, Long size) {
    verify(filename).isNotNull();
    verify(data).isNotNull();
    verify(md5).isNotNull();

    ResourceModel model = new ResourceModel();
    model.setContent(data);
    model.setMd5sum(new String(Hex.encodeHex(md5)));
    model.setName(filename);
    model.setOriginalName(filename);
    model.setOwner(auth.getCurrentUser());
    model.setCreatedAt(new Date());
    model.setLastAccess(new Date());
    model.setSize(size);
    repo.saveAndFlush(model);
    return model;
  }

}
