package de.bitnoise.sonferenz.web.pages.resources;

import java.sql.Blob;

import org.apache.wicket.markup.html.DynamicWebResource;
import org.apache.wicket.markup.html.image.resource.BlobImageResource;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;

import de.bitnoise.sonferenz.model.ResourceModel;

public class PageImages extends DynamicImageResource {

  private byte[] data;

  public PageImages(ResourceModel picture) {
    data = picture.getContent();
  }

  @Override
  protected byte[] getImageData() {
    return data;
  }

  
}
