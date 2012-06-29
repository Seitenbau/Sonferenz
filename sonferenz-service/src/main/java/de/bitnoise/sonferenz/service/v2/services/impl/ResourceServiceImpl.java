package de.bitnoise.sonferenz.service.v2.services.impl;

import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.bitnoise.sonferenz.model.FileResourceModel;
import de.bitnoise.sonferenz.model.ResourceModel;
import de.bitnoise.sonferenz.repo.FileResourceRepository;
import de.bitnoise.sonferenz.repo.ResourceRepository;
import de.bitnoise.sonferenz.service.v2.services.AuthenticationService;
import de.bitnoise.sonferenz.service.v2.services.ResourceService;
import static de.bitnoise.sonferenz.service.v2.services.verify.VerifyParam.*;

@Service
public class ResourceServiceImpl implements ResourceService
{

  @Autowired
  FileResourceRepository repo;

  @Autowired
  ResourceRepository repoRes;

  @Autowired
  AuthenticationService auth;

  @Override
  public FileResourceModel storeResource(String filename, byte[] data,
      byte[] md5, Long size)
  {
    verify(filename).isNotNull();
    verify(data).isNotNull();
    verify(md5).isNotNull();

    FileResourceModel model = new FileResourceModel();
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

  @Override
  public void registerPlay(ResourceModel video)
  {
    video.setLastAccess(new Date());
    Long old = video.getAccessCount();
    if (old == null)
    {
      old = 1L;
    }
    video.setAccessCount(old + 1);

    repoRes.save(video);
  }

}
